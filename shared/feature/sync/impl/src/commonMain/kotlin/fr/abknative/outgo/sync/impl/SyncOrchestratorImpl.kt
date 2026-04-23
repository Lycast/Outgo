package fr.abknative.outgo.sync.impl

import fr.abknative.outgo.auth.api.provider.SessionProvider
import fr.abknative.outgo.auth.api.usecase.LogoutUseCase
import fr.abknative.outgo.auth.api.usecase.ObserveUserSessionUseCase
import fr.abknative.outgo.core.api.KeyValueStorage
import fr.abknative.outgo.core.api.LocalDataMigrator
import fr.abknative.outgo.core.api.NetworkMonitor
import fr.abknative.outgo.core.api.logs.AppLogger
import fr.abknative.outgo.core.api.logs.Result
import fr.abknative.outgo.core.api.time.TimeProvider
import fr.abknative.outgo.sync.api.SyncEvent
import fr.abknative.outgo.sync.api.SyncManager
import fr.abknative.outgo.sync.api.SyncOrchestrator
import fr.abknative.outgo.wallet.api.repository.OperationRepository
import fr.abknative.outgo.wallet.api.repository.WalletRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlin.time.Duration.Companion.milliseconds

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
internal class SyncOrchestratorImpl(
    private val syncManager: SyncManager,
    private val sessionProvider: SessionProvider,
    private val observeUserSession: ObserveUserSessionUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val walletRepository: WalletRepository,
    private val operationRepository: OperationRepository,
    private val networkMonitor: NetworkMonitor,
    private val localDataMigrator: LocalDataMigrator,
    private val storage: KeyValueStorage,
    private val timeProvider: TimeProvider,
    private val scope: CoroutineScope
) : SyncOrchestrator {

    private val _syncEvents = MutableSharedFlow<SyncEvent>()
    override val syncEvents = _syncEvents.asSharedFlow()

    private var activeSyncJob: Job? = null

    companion object {
        private const val LAST_SYNC_KEY = "last_sync_timestamp"
        private const val SYNC_THRESHOLD_MS = 12 * 60 * 60 * 1000L
        private const val DEBOUNCE_DELAY_MS = 10000L
        private const val TAG = "SyncOrchestrator"
    }

    override fun start() {
        AppLogger.get()?.i(TAG, "Sync Orchestrator Startup")
        checkStartupSync()
        startObservingPendingData()
        startObservingLogins()
    }

    override fun triggerManualSync() {
        val currentUserId = sessionProvider.getCurrentUserId()
        if (currentUserId.startsWith("local_")) return

        val lastSync = storage.getLong(LAST_SYNC_KEY, 0L)
        if (lastSync == 0L) {
            performInitialLoginCheck(currentUserId)
        } else {
            executeSafeFullSync(reason = "Manual refresh", emitErrorEvent = true)
        }
    }

    override fun resolveConflictDownloadCloud() {
        AppLogger.get()?.i(TAG, "Conflict resolved: User chose to keep Cloud data.")
        executeSafeFullSync(reason = "Conflict resolved - Downloading cloud data", emitErrorEvent = true)
    }

    override fun resolveConflictCancelLogin() {
        AppLogger.get()?.i(TAG, "Conflict resolved: User canceled login. Reverting to local data.")
        scope.launch {
            logoutUseCase(displayLocalData = false)
        }
    }

    private fun checkStartupSync() {
        scope.launch {
            val currentUserId = sessionProvider.observeUserId().first()

            if (currentUserId.startsWith("local_")) {
                AppLogger.get()?.d(TAG, "Startup Pull skipped: Local session active.")
                return@launch
            }

            observeUserSession().filterNotNull().first()

            val lastSync = storage.getLong(LAST_SYNC_KEY, 0L)

            if (lastSync == 0L) {
                performInitialLoginCheck(currentUserId)
            } else {
                val now = timeProvider.now()
                if (now - lastSync > SYNC_THRESHOLD_MS) {
                    executeSafeFullSync(reason = "Startup > 12h", emitErrorEvent = false)
                }
            }
        }
    }

    private fun startObservingPendingData() {
        val hasPendingDataFlow = combine(
            walletRepository.observePendingWallets(),
            operationRepository.observePendingOperations()
        ) { pendingWallets, pendingOps ->
            pendingWallets.isNotEmpty() || pendingOps.isNotEmpty()
        }

        combine(
            hasPendingDataFlow,
            networkMonitor.isConnected,
            sessionProvider.observeUserId(),
            observeUserSession()
        ) { hasPendingData, isConnected, userId, session ->
            hasPendingData && isConnected && !userId.startsWith("local_") && session != null
        }
            .distinctUntilChanged()
            .filter { shouldSync ->
                val lastSync = storage.getLong(LAST_SYNC_KEY, 0L)
                shouldSync && lastSync != 0L
            }
            .debounce(DEBOUNCE_DELAY_MS.milliseconds)
            .onEach {
                AppLogger.get()?.i(TAG, "Conditions met. Triggering Push.")
                syncManager.syncOut()
            }
            .launchIn(scope)
    }

    private fun startObservingLogins() {
        scope.launch {
            sessionProvider.observeUserId()
                .drop(1)
                .distinctUntilChanged()
                .collect { newUserId ->
                    if (!newUserId.startsWith("local_")) {
                        // ✨ CHANGEMENT : On délègue à la nouvelle méthode
                        performInitialLoginCheck(newUserId)
                    } else {
                        AppLogger.get()?.i(TAG, "Switched back to local identity: $newUserId")
                    }
                }
        }
    }

    private fun performInitialLoginCheck(userId: String) {
        scope.launch {
            AppLogger.get()?.i(TAG, "Performing initial login check for $userId...")

            when (val hasRemoteResult = syncManager.hasRemoteData()) {
                is Result.Success -> {
                    val serverHasData = hasRemoteResult.data
                    val lastLocalId = sessionProvider.getLastLocalId()

                    if (serverHasData) {
                        AppLogger.get()?.i(TAG, "Server has data. Emitting Conflict event.")
                        _syncEvents.emit(SyncEvent.ConflictRequiresResolution)
                    } else {
                        AppLogger.get()?.i(TAG, "Server is empty. Performing silent migration.")
                        if (lastLocalId != null) {
                            localDataMigrator.checkConflictAndMigrate(userId, lastLocalId)
                        }
                        executeSafeFullSync(reason = "Initial push after migration", emitErrorEvent = true)
                    }
                }
                is Result.Error -> {
                    AppLogger.get()?.e(TAG, "Failed to check remote data", hasRemoteResult.error)
                    _syncEvents.emit(SyncEvent.Error(hasRemoteResult.error))
                }
            }
        }
    }

    private fun executeSafeFullSync(reason: String, emitErrorEvent: Boolean = false) {
        if (activeSyncJob?.isActive == true) {
            AppLogger.get()?.d(TAG, "Skipping sync ($reason) - A sync is already running.")
            return
        }

        activeSyncJob = scope.launch {
            AppLogger.get()?.i(TAG, "Starting full sync: $reason")
            val result = syncManager.syncAll()

            if (result is Result.Success) {
                AppLogger.get()?.i(TAG, "Full sync successful. Resetting timer.")
                storage.putLong(LAST_SYNC_KEY, timeProvider.now())
            } else if (result is Result.Error) {
                AppLogger.get()?.w(TAG, "Full sync failed: $reason")
                if (emitErrorEvent) {
                    _syncEvents.emit(SyncEvent.Error(result.error))
                }
            }
        }
    }
}