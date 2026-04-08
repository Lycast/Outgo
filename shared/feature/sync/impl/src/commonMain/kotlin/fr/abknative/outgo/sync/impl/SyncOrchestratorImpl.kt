package fr.abknative.outgo.sync.impl

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
        val lastSync = storage.getLong(LAST_SYNC_KEY, 0L)
        val now = timeProvider.now()

        if (now - lastSync > SYNC_THRESHOLD_MS) {
            AppLogger.get()?.i(TAG, "Last sync > 12h. Launching startup Pull.")
            scope.launch {
                val result = syncManager.syncAll()

                if (result is Result.Success) {
                    AppLogger.get()?.i(TAG, "Startup Pull successful. Resetting 12h timer.")
                    storage.putLong(LAST_SYNC_KEY, timeProvider.now())
                } else {
                    AppLogger.get()?.w(TAG, "Startup Pull failed. Will retry on next startup.")
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
            networkMonitor.isConnected
        ) { hasPendingData, isConnected ->
            hasPendingData && isConnected
        }
            .distinctUntilChanged()
            .filter { shouldSync -> shouldSync }
            .debounce(DEBOUNCE_DELAY_MS.milliseconds)
            .onEach {
                AppLogger.get()?.i(TAG, "Network OK + Pending data + 10s quiet period; Triggering Push.")
                syncManager.syncOut()
            }
            .launchIn(scope)
    }
}