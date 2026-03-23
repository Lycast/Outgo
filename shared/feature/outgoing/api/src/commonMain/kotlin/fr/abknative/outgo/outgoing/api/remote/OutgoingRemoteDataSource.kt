package fr.abknative.outgo.outgoing.api.remote

import fr.abknative.outgo.core.api.AppException
import fr.abknative.outgo.core.api.Result
import fr.abknative.outgo.outgoing.api.model.Budget
import fr.abknative.outgo.outgoing.api.model.Outgoing

interface OutgoingRemoteDataSource {
    suspend fun pushData(outgoings: List<Outgoing>, budgets: List<Budget>): Result<Unit, AppException>
    suspend fun pullData(since: Long): Result<Pair<List<Outgoing>, List<Budget>>, AppException>
}