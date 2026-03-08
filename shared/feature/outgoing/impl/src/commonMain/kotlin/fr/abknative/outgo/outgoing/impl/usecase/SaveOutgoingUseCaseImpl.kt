package fr.abknative.outgo.outgoing.impl.usecase

import fr.abknative.outgo.core.api.AppException
import fr.abknative.outgo.core.api.Result
import fr.abknative.outgo.core.api.SyncStatus
import fr.abknative.outgo.core.api.TimeProvider
import fr.abknative.outgo.outgoing.api.BillingCycle
import fr.abknative.outgo.outgoing.api.OutgoingError
import fr.abknative.outgo.outgoing.api.model.Outgoing
import fr.abknative.outgo.outgoing.api.repository.OutgoingRepository
import fr.abknative.outgo.outgoing.api.usecase.SaveOutgoingUseCase
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

internal class SaveOutgoingUseCaseImpl(
    private val repository: OutgoingRepository,
    private val timeProvider: TimeProvider
) : SaveOutgoingUseCase {

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun invoke(
        name: String,
        amountInCents: Long,
        cycle: BillingCycle,
        billingDay: Int,
        billingMonth: Int?
    ): Result<Unit, AppException> {


        if (name.isBlank()) return Result.Error(OutgoingError.EmptyName())
        if (amountInCents <= 0) return Result.Error(OutgoingError.InvalidAmount())
        if (billingDay !in 1..31) return Result.Error(OutgoingError.InvalidDate())
        val finalBillingMonth = when (cycle) {
            BillingCycle.MONTHLY -> { null }
            BillingCycle.YEARLY -> { if (billingMonth == null || billingMonth !in 1..12) { return Result.Error(OutgoingError.InvalidDate()) } else billingMonth }
            BillingCycle.UNKNOWN -> return Result.Error(OutgoingError.UnknownCycle())
        }

        val currentTime = timeProvider.now()
        val outgoing = Outgoing(
            id = Uuid.random().toString(),
            name = name.trim(),
            amountInCents = amountInCents,
            cycle = cycle,
            billingDay = billingDay,
            billingMonth = finalBillingMonth,
            createdAt = currentTime,
            updatedAt = currentTime,
            isDeleted = false,
            syncStatus = SyncStatus.PENDING_CREATE
        )

        return repository.upsert(outgoing)
    }
}