package fr.abknative.outgo.android.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import fr.abknative.outgo.core.api.SyncStatus
import fr.abknative.outgo.core.ui.Palette
import fr.abknative.outgo.outgoing.api.Recurrence
import fr.abknative.outgo.outgoing.api.model.Outgoing
import kotlin.math.absoluteValue

// --- Formatage de la Monnaie ---
val Long.uiAmount: String
    get() {
        val isNegative = this < 0
        val absoluteValue = this.absoluteValue

        val euros = absoluteValue / 100
        val cents = absoluteValue % 100

        val formattedCents = cents.toString().padStart(2, '0')

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
        SyncStatus.SYNCED -> Color(0xFF4CAF50)
        SyncStatus.PENDING_CREATE -> Color(0xFF2196F3)
        SyncStatus.PENDING_UPDATE -> Color(0xFFFF9800)
        SyncStatus.PENDING_DELETE -> Color(0xFFF44336)
        SyncStatus.UNKNOWN -> Color.Gray
    }

val Recurrence.uiRecurrenceColor: Color
    get() = when (this) {
        Recurrence.YEARLY -> Color(Palette.BrandSecondary)
        Recurrence.MONTHLY -> Color(Palette.BrandTertiary)
        Recurrence.UNKNOWN -> Color.Gray
    }

// --- 3. L'Entité Outgoing ---
val Outgoing.uiTitle: String
    @Composable get() = this.name.ifBlank { DashboardLabels.DEFAULT_NAME }

val Outgoing.uiDueDayLabel: String
    @Composable get() = if (this.recurrence == Recurrence.YEARLY && this.dueMonth != null) {
        val monthLabel = getMonthName(this.dueMonth!!)
        "${DashboardLabels.DUE_PREFIX} $dueDay $monthLabel"
    } else {
        "${DashboardLabels.DUE_PREFIX} $dueDay"
    }

val Outgoing.uiFrequencySummary: String
    @Composable get() = when (this.recurrence) {
        Recurrence.MONTHLY -> FormLabels.CYCLE_MONTHLY
        Recurrence.YEARLY -> FormLabels.CYCLE_YEARLY
        Recurrence.UNKNOWN -> ""
    }

@Composable
fun getMonthName(month: Int): String = when (month) {
    1 -> DashboardLabels.MONTH_1 ; 2 -> DashboardLabels.MONTH_2 ; 3 -> DashboardLabels.MONTH_3
    4 -> DashboardLabels.MONTH_4 ; 5 -> DashboardLabels.MONTH_5 ; 6 -> DashboardLabels.MONTH_6
    7 -> DashboardLabels.MONTH_7 ; 8 -> DashboardLabels.MONTH_8 ; 9 -> DashboardLabels.MONTH_9
    10 -> DashboardLabels.MONTH_10; 11 -> DashboardLabels.MONTH_11; 12 -> DashboardLabels.MONTH_12
    else -> ""
}