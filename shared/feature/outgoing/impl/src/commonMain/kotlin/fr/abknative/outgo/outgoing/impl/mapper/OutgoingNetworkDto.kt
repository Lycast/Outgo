package fr.abknative.outgo.outgoing.impl.mapper

import fr.abknative.outgo.core.api.SyncStatus
import fr.abknative.outgo.outgoing.api.model.Outgoing
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class OutgoingNetworkDto(
    @SerialName("id") val id: String,
    @SerialName("name") val name: String,
    @SerialName("amount_cents") val amountInCents: Long,
    @SerialName("billing_cycle") val cycle: String,
    @SerialName("billing_day") val billingDay: Int,
    @SerialName("billing_month") val billingMonth: Int? = null,
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
        billingMonth = this.billingMonth,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        isDeleted = this.isDeleted,
        // RÈGLE MÉTIER : Un objet qui provient du réseau est, par définition, déjà synchronisé.
        syncStatus = SyncStatus.SYNCED
    )
}