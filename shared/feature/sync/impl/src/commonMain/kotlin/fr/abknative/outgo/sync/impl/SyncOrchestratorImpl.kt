package fr.abknative.outgo.sync.impl

import fr.abknative.outgo.auth.api.provider.SessionProvider
import fr.abknative.outgo.auth.api.usecase.ObserveUserSessionUseCase
import fr.abknative.outgo.core.api.KeyValueStorage
import fr.abknative.outgo.core.api.NetworkMonitor
import fr.abknative.outgo.core.api.TimeProvider
import fr.abknative.outgo.core.api.logs.AppLogger
import fr.abknative.outgo.core.api.logs.Result
import fr.abknative.outgo.sync.api.SyncManager
import fr.abknative.outgo.sync.api.SyncOrchestrator
import fr.abknative.outgo.wallet.api.repository.OperationRepository
import fr.abknative.outgo.wallet.api.repository.WalletRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
internal class SyncOrchestratorImpl(
    private val syncManager: SyncManager,
    private val sessionProvider: SessionProvider,
    private val observeUserSession: ObserveUserSessionUseCase,
    private val walletRepository: WalletRepository,
    private val operationRepository: OperationRepository,
    private val networkMonitor: NetworkMonitor,
    private val storage: KeyValueStorage,
    private val timeProvider: TimeProvider,
    private val scope: CoroutineScope
) : SyncOrchestrator {

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
    }

    private fun checkStartupSync() {
        scope.launch {

            val session = observeUserSession().filterNotNull().first()
            val currentUserId = sessionProvider.observeUserId().first()

            if (currentUserId.startsWith("local_")) {
                AppLogger.get()?.d(TAG, "Startup Pull skipped: Local session active.")
                return@launch
            }

            val lastSync = storage.getLong(LAST_SYNC_KEY, 0L)
            val now = timeProvider.now()

            if (now - lastSync > SYNC_THRESHOLD_MS) {
                AppLogger.get()?.i(TAG, "Last sync > 12h. Launching startup Pull.")
                val result = syncManager.syncAll()

                if (result is Result.Success) {
                    AppLogger.get()?.i(TAG, "Startup Pull successful. Resetting 12h timer.")
                    storage.putLong(LAST_SYNC_KEY, timeProvider.now())
                } else {
                    AppLogger.get()?.w(TAG, "Startup Pull failed. Will retry later.")
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
            .filter { shouldSync -> shouldSync }
            .debounce(DEBOUNCE_DELAY_MS.milliseconds)
            .onEach {
                AppLogger.get()?.i(TAG, "Conditions met. Triggering Push.")
                syncManager.syncOut()
            }
            .launchIn(scope)
    }
}