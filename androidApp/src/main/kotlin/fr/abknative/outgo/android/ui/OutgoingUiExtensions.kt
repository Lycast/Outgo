package fr.abknative.outgo.android.ui

import androidx.compose.ui.graphics.Color
import fr.abknative.outgo.core.api.SyncStatus
import fr.abknative.outgo.outgoing.api.Recurrence
import fr.abknative.outgo.outgoing.api.model.Outgoing

// --- 1. Formattage de la Monnaie ---
val Long.uiAmount: String
    get() {
        val euros = this / 100.0
        // Idéalement, on utilisera NumberFormat plus tard, mais pour le MVP :
        val formatted = euros.toString().replace(".0", "")
        return "$formatted ${CommonLabels.CURRENCY_SYMBOL}"
    }

// --- 2. Enums (Cycles et Sync) ---
val Recurrence.uiLabel: String
    get() = when (this) {
        Recurrence.MONTHLY -> "/ mois"
        Recurrence.YEARLY -> "/ an"
        Recurrence.UNKNOWN -> ""
    }

val SyncStatus.uiColor: Color
    get() = when (this) {
        SyncStatus.SYNCED -> Color(0xFF4CAF50) // Vert
        SyncStatus.PENDING_CREATE -> Color(0xFF2196F3) // Bleu
        SyncStatus.PENDING_UPDATE -> Color(0xFFFF9800) // Orange
        SyncStatus.PENDING_DELETE -> Color(0xFFF44336) // Rouge
        SyncStatus.UNKNOWN -> Color.Gray
    }

// --- 3. L'Entité Outgoing ---
val Outgoing.uiTitle: String
    get() = this.name.ifBlank { "Dépense sans nom" }
val Outgoing.uiAmountLabel: String
    get() = "${this.amountInCents.uiAmount} ${this.recurrence.uiLabel}"
val Outgoing.uiBillingDayLabel: String
    get() = "Le ${this.dueDay} du mois"