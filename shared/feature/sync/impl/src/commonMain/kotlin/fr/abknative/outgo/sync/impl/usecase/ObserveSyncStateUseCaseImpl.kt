package fr.abknative.outgo.sync.impl.usecase

import fr.abknative.outgo.auth.api.usecase.ObserveUserSessionUseCase
import fr.abknative.outgo.core.api.NetworkMonitor
import fr.abknative.outgo.sync.api.SyncManager
import fr.abknative.outgo.sync.api.model.SyncState
import fr.abknative.outgo.sync.api.usecase.ObserveSyncStateUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged

internal class ObserveSyncStateUseCaseImpl(
    private val networkMonitor: NetworkMonitor,
    private val observeUserSession: ObserveUserSessionUseCase,
    private val syncManager: SyncManager
) : ObserveSyncStateUseCase {

    override operator fun invoke(): Flow<SyncState> {
        return combine(
            observeUserSession(),
            networkMonitor.isConnected,
            syncManager.isSyncing,
            syncManager.syncError
        ) { session, isConnected, isSyncing, syncError ->

            when {
                session == null || !session.isEmailVerified -> SyncState.UNAUTHENTICATED
                !isConnected -> SyncState.OFFLINE
                isSyncing -> SyncState.IN_PROGRESS
                syncError != null -> SyncState.ERROR
                else -> SyncState.UP_TO_DATE
            }
        }.distinctUntilChanged()
    }
}