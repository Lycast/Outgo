package fr.abknative.outgo.outgoing.impl.usecase

import fr.abknative.outgo.core.api.AppException
import fr.abknative.outgo.core.api.Result
import fr.abknative.outgo.core.api.SyncStatus
import fr.abknative.outgo.core.api.TimeProvider
import fr.abknative.outgo.outgoing.api.OutgoingError
import fr.abknative.outgo.outgoing.api.Recurrence
import fr.abknative.outgo.outgoing.api.model.Outgoing
import fr.abknative.outgo.outgoing.api.repository.OutgoingRepository
import fr.abknative.outgo.outgoing.api.usecase.SaveOutgoingUseCase
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

internal class SaveOutgoingUseCaseImpl(
    private val repository: OutgoingRepository,
    private val timeProvider: TimeProvider
) : SaveOutgoingUseCase {

    /**
     * Implementation details:
     * 1. Validates input bounds and enforces recurrence rules (e.g., stripping dueMonth for monthly items).
     * 2. Resolves offline-first metadata ([createdAt], [updatedAt]).
     * 3. Determines the correct [SyncStatus] state transition:
     * - A record that has never been synced remotely remains in [SyncStatus.PENDING_CREATE] even if updated locally.
     * - Already synced records transition to [SyncStatus.PENDING_UPDATE].
     */
    @OptIn(ExperimentalUuidApi::class)
    override suspend fun invoke(
        id: String?,
        name: String,
        amountInCents: Long,
        recurrence: Recurrence,
        dueDay: Int,
        dueMonth: Int?
    ): Result<Unit, AppException> {

        if (name.isBlank()) return Result.Error(OutgoingError.EmptyName())
        if (amountInCents <= 0) return Result.Error(OutgoingError.InvalidAmount())
        if (dueDay !in 1..31) return Result.Error(OutgoingError.InvalidDate())

        val finalBillingMonth = when (recurrence) {
            Recurrence.MONTHLY -> null
            Recurrence.YEARLY -> {
                if (dueMonth == null || dueMonth !in 1..12) return Result.Error(OutgoingError.InvalidDate())
                dueMonth
            }

            Recurrence.UNKNOWN -> return Result.Error(OutgoingError.UnknownCycle())
        }

        val isNew = id.isNullOrBlank()
        val finalId = if (isNew) Uuid.random().toString() else id
        val existingOutgoing = if (!isNew) repository.getOutgoingById(finalId) else null

        val currentTime = timeProvider.now()

        val finalSyncStatus = when {
            existingOutgoing == null -> SyncStatus.PENDING_CREATE
            existingOutgoing.syncStatus == SyncStatus.PENDING_CREATE -> SyncStatus.PENDING_CREATE
            else -> SyncStatus.PENDING_UPDATE
        }

        val outgoing = Outgoing(
            id = finalId,
            name = name.trim(),
            amountInCents = amountInCents,
            recurrence = recurrence,
            dueDay = dueDay,
            dueMonth = finalBillingMonth,
            createdAt = existingOutgoing?.createdAt ?: currentTime,
            updatedAt = currentTime,
            isDeleted = false,
            syncStatus = finalSyncStatus
        )

        return if (existingOutgoing == null) {
            repository.insert(outgoing)
        } else {
            repository.update(outgoing)
        }
    }
}