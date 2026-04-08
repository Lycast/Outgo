package fr.abknative.outgo.sync.api.usecase

import fr.abknative.outgo.sync.api.model.SyncState
import kotlinx.coroutines.flow.Flow

interface ObserveSyncStateUseCase {
    operator fun invoke(): Flow<SyncState>
}