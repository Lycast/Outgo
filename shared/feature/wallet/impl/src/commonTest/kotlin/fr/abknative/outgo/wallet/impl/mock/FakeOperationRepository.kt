package fr.abknative.outgo.wallet.impl.mock

import fr.abknative.outgo.core.api.EpochMillis
import fr.abknative.outgo.core.api.SyncStatus
import fr.abknative.outgo.core.api.logs.AppException
import fr.abknative.outgo.core.api.logs.Result
import fr.abknative.outgo.wallet.api.model.Operation
import fr.abknative.outgo.wallet.api.repository.OperationRepository
import kotlinx.coroutines.flow.Flow

class FakeOperationRepository : OperationRepository {
    var lastSavedOperation: Operation? = null
    var operationToReturn: Operation? = null
    var listToObserve: List<Operation> = emptyList()

    override suspend fun save(operation: Operation): Result<Unit, AppException> {
        lastSavedOperation = operation
        return Result.Success(Unit)
    }

    override suspend fun getOperationById(id: String): Operation? = operationToReturn

    override fun observeOperationsByPeriod(from: EpochMillis, to: EpochMillis): Flow<List<Operation>> {
        return kotlinx.coroutines.flow.flowOf(listToObserve)
    }

    override suspend fun markAsDeleted(id: String): Result<Unit, AppException> = Result.Success(Unit)

    override suspend fun getPendingOperations(): Result<List<Operation>, AppException> = Result.Success(emptyList())

    override suspend fun updateSyncStatus(id: String, status: SyncStatus): Result<Unit, AppException> = Result.Success(Unit)

    override suspend fun syncFromServer(operations: List<Operation>): Result<Unit, AppException> = Result.Success(Unit)
}