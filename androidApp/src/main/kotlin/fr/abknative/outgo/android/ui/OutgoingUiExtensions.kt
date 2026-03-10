package fr.abknative.outgo.android.ui

import androidx.compose.ui.graphics.Color
import fr.abknative.outgo.core.api.SyncStatus
import fr.abknative.outgo.outgoing.api.Recurrence
import fr.abknative.outgo.outgoing.api.model.Outgoing
import kotlin.math.absoluteValue

// --- 1. Formatage de la Monnaie (Précision & Lisibilité) ---
val Long.uiAmount: String
    get() {
        val isNegative = this < 0
        val absoluteValue = this.absoluteValue

        val euros = absoluteValue / 100
        val cents = absoluteValue % 100

        val formattedCents = cents.toString().padStart(2, '0')

        // 3. Formatage des milliers
        val eurosString = euros.toString()
            .reversed()
            .chunked(3)
            .joinToString(" ")
            .reversed()

        val prefix = if (isNegative) "-" else ""

        return "$prefix$eurosString,$formattedCents ${CommonLabels.CURRENCY_SYMBOL}"
    }

val SyncStatus.uiColor: Color
    get() = when (this) {
        SyncStatus.SYNCED -> Color(0xFF4CAF50) // Vert (Ok)
        SyncStatus.PENDING_CREATE -> Color(0xFF2196F3) // Bleu (Nouveau)
        SyncStatus.PENDING_UPDATE -> Color(0xFFFF9800) // Orange (Modifié)
        SyncStatus.PENDING_DELETE -> Color(0xFFF44336) // Rouge (Supprimé)
        SyncStatus.UNKNOWN -> Color.Gray
    }

// --- 3. L'Entité Outgoing (Mapping pour les Cards) ---
val Outgoing.uiTitle: String
    get() = this.name.ifBlank { "Sans nom" }

val Outgoing.uiDueDayLabel: String
    get() = if (this.recurrence == Recurrence.YEARLY && this.dueMonth != null) {
        val monthLabel = getMonthName(this.dueMonth!!)
        "Le $dueDay $monthLabel"
    } else {
        "Le $dueDay du mois"
    }

val Outgoing.uiFrequencySummary: String
    get() = when (this.recurrence) {
        Recurrence.MONTHLY -> "Mensuel"
        Recurrence.YEARLY -> "Annuel"
        Recurrence.UNKNOWN -> ""
    }

fun getMonthName(month: Int): String = when (month) {
    0 -> "" ; 1 -> "Janvier" ; 2 -> "Février" ; 3 -> "Mars"
    4 -> "Avril"; 5 -> "Mai" ; 6 -> "Juin" ; 7 -> "Juillet"
    8 -> "Août"; 9 -> "Septembre" ; 10 -> "Octobre"
    11 -> "Novembre" ; 12 -> "Décembre"
    else -> ""
}