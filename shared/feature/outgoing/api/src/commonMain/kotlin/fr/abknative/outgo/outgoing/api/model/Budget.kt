package fr.abknative.outgo.outgoing.api.model

import fr.abknative.outgo.core.api.EpochMillis
import fr.abknative.outgo.core.api.SyncStatus

/**
 * Represents the user's global financial context (Income and Budget).
 *
 * @property id The unique identifier (use "default" for a single-budget configuration).
 * @property monthlyIncomeInCents The total monthly income, stored in cents to prevent precision errors.
 * @property createdAt Initial creation timestamp of this budget (in milliseconds).
 * @property updatedAt Timestamp of the last local or remote modification.
 * @property syncStatus Current synchronization state with the server (PENDING_CREATE, SYNCED, etc.).
 * Allows the sync engine to determine whether this object needs to be uploaded.
 */
data class Budget(
    val id: String = "default",
    val monthlyIncomeInCents: Long = 0L,

    // Métadonnées Offline-First
    val createdAt: EpochMillis,
    val updatedAt: EpochMillis,
    val syncStatus: SyncStatus = SyncStatus.PENDING_CREATE
)