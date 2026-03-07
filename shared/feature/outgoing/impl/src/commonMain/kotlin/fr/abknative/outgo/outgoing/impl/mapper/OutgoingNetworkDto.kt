package fr.abknative.outgo.outgoing.impl.mapper

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import fr.abknative.outgo.outgoing.api.model.Outgoing
import fr.abknative.outgo.outgoing.api.BillingCycle
import fr.abknative.outgo.core.api.SyncStatus

@Serializable
internal data class OutgoingNetworkDto(
    @SerialName("id") val id: String,
    @SerialName("name") val name: String,
    @SerialName("amount_cents") val amountInCents: Long,
    @SerialName("billing_cycle") val cycle: String,
    @SerialName("billing_day") val billingDay: Int,
    @SerialName("created_at") val createdAt: Long,
    @SerialName("updated_at") val updatedAt: Long,
    @SerialName("is_deleted") val isDeleted: Boolean = false
)

internal fun OutgoingNetworkDto.toDomain(): Outgoing {
    return Outgoing(
        id = this.id,
        name = this.name,
        amountInCents = this.amountInCents,
        cycle = mapToBillingCycle(this.cycle),
        billingDay = this.billingDay,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        isDeleted = this.isDeleted,
        // RÈGLE MÉTIER : Un objet qui provient du réseau est, par définition, déjà synchronisé.
        syncStatus = SyncStatus.SYNCED
    )
}

/**
 * Safely parses a string into a [BillingCycle].
 * Returns [BillingCycle.UNKNOWN] if the value is unrecognized.
 */
private fun mapToBillingCycle(value: String): BillingCycle {
    return BillingCycle.entries.find {
        it.name.equals(value, ignoreCase = true)
    } ?: BillingCycle.UNKNOWN
}