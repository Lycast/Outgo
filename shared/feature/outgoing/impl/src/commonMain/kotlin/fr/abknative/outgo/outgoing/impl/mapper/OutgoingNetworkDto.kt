package fr.abknative.outgo.outgoing.impl.mapper

import fr.abknative.outgo.core.api.SyncStatus
import fr.abknative.outgo.outgoing.api.model.Outgoing
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class OutgoingNetworkDto(
    @SerialName("id") val id: String,
    @SerialName("budget_id") val budgetId: String,
    @SerialName("name") val name: String,
    @SerialName("amount_cents") val amountInCents: Long,
    @SerialName("recurrence") val recurrence: String,
    @SerialName("due_day") val dueDay: Int,
    @SerialName("due_month") val dueMonth: Int? = null,
    @SerialName("created_at") val createdAt: Long,
    @SerialName("updated_at") val updatedAt: Long,
    @SerialName("is_deleted") val isDeleted: Boolean = false
)

internal fun OutgoingNetworkDto.toDomain(): Outgoing {
    return Outgoing(
        id = this.id,
        name = this.name,
        amountInCents = this.amountInCents,
        recurrence = mapToRecurrence(this.recurrence),
        dueDay = this.dueDay,
        dueMonth = this.dueMonth,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        isDeleted = this.isDeleted,
        // RÈGLE MÉTIER : Un objet qui provient du réseau est, par définition, déjà synchronisé.
        syncStatus = SyncStatus.SYNCED
    )
}