package fr.abknative.outgo.android.ui

import androidx.compose.ui.graphics.Color
import fr.abknative.outgo.core.api.SyncStatus
import fr.abknative.outgo.outgoing.api.Recurrence
import fr.abknative.outgo.outgoing.api.model.Outgoing

// --- 1. Formatage de la Monnaie (Précision & Lisibilité) ---
val Long.uiAmount: String
    get() {
        val euros = this / 100
        val cents = this % 100
        val formattedCents = cents.toString().padStart(2, '0')

        // Formatage des milliers pour la lisibilité (ex: 1 500)
        val eurosString = euros.toString()
            .reversed()
            .chunked(3)
            .joinToString(" ")
            .reversed()

        return "$eurosString,$formattedCents ${CommonLabels.CURRENCY_SYMBOL}"
    }

// --- 2. Enums (Récurrence et Synchronisation) ---
val Recurrence.uiLabel: String
    get() = when (this) {
        Recurrence.MONTHLY -> "/ mois"
        Recurrence.YEARLY -> "/ an"
        Recurrence.UNKNOWN -> ""
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

val Outgoing.uiAmountLabel: String
    get() = "${this.amountInCents.uiAmount} ${this.recurrence.uiLabel}"

val Outgoing.uiDueDayLabel: String
    get() = if (this.recurrence == Recurrence.YEARLY && this.dueMonth != null) {
        val monthLabel = getMonthName(this.dueMonth!!)
        "Le $dueDay $monthLabel"
    } else {
        "Le $dueDay du mois"
    }

// --- 4. Helpers Internes ---
private fun getMonthName(month: Int): String = when (month) {
    1 -> "Janvier" ; 2 -> "Février" ; 3 -> "Mars" ; 4 -> "Avril"
    5 -> "Mai" ; 6 -> "Juin" ; 7 -> "Juillet" ; 8 -> "Août"
    9 -> "Septembre" ; 10 -> "Octobre" ; 11 -> "Novembre" ; 12 -> "Décembre"
    else -> ""
}

// --- 5. Formatage de la date du State ---
/**
 * Transforme le format "9|3" envoyé par le Presenter en "9 Mars".
 */
val String.uiTodayDate: String
    get() {
        val parts = this.split("|")
        if (parts.size != 2) return ""
        val day = parts[0]
        val month = parts[1].toIntOrNull() ?: return ""
        return "$day ${getMonthName(month)}"
    }