package fr.abknative.outgo.android.ui.list

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import fr.abknative.outgo.android.core.CommonLabels
import fr.abknative.outgo.android.core.designsystem.AppTheme
import fr.abknative.outgo.android.core.designsystem.toColor
import fr.abknative.outgo.list.api.ListViewMode
import fr.abknative.outgo.list.api.ProjectedFilter
import fr.abknative.outgo.list.api.StandardFilter
import fr.abknative.outgo.wallet.api.model.Operation
import fr.abknative.outgo.wallet.api.model.operation.Recurrence

/**
 * Maps a [Recurrence] to its corresponding UI Color using the AppTheme.
 */
@Composable
fun Recurrence.getUiColor(): Color {
    return when (this) {
        Recurrence.YEARLY -> AppTheme.colors.textSecondary.toColor()
        Recurrence.MONTHLY -> AppTheme.colors.primary.toColor()
        Recurrence.WEEKLY -> AppTheme.colors.secondary.toColor()
        Recurrence.UNIQUE -> AppTheme.colors.tertiary.toColor()
        else -> AppTheme.colors.surface200.toColor()
    }
}


// --- Operation Entity Extensions ---


/**
 * Returns the display title for the operation, falling back to a default name if blank.
 */
val Operation.uiTitle: String
    @Composable get() = this.name.ifBlank { ListLabels.DEFAULT_NAME }


/**
 * Returns the localized label for a [Recurrence] type.
 */
val Recurrence.uiLabel: String
    @Composable get() = when (this) {
        Recurrence.UNIQUE -> CommonLabels.CYCLE_UNIQUE
        Recurrence.WEEKLY -> CommonLabels.CYCLE_WEEKLY
        Recurrence.MONTHLY -> CommonLabels.CYCLE_MONTHLY
        Recurrence.YEARLY -> CommonLabels.CYCLE_YEARLY
        Recurrence.UNKNOWN -> ""
    }


/**
 * Returns the localized label for a [Recurrence] type.
 */
val Recurrence.uiFormattedLabel: String
    @Composable get() = when (this) {
        Recurrence.UNIQUE -> ListLabels.CYCLE_UNIQUE_FORMATTED
        Recurrence.WEEKLY -> ListLabels.CYCLE_WEEKLY_FORMATTED
        Recurrence.MONTHLY -> ListLabels.CYCLE_MONTHLY_FORMATTED
        Recurrence.YEARLY -> ListLabels.CYCLE_YEARLY_FORMATTED
        Recurrence.UNKNOWN -> ""
    }


// --- List Filters UI Mapping ---


/**
 * Returns the localized label for a [ListViewMode].
 */
val ListViewMode.uiLabel: String
    @Composable get() = when (this) {
        ListViewMode.PROJECTED -> ListLabels.TAB_PROJECTED
        ListViewMode.STANDARD -> ListLabels.TAB_STANDARD
    }


/**
 * Returns the localized label for a [ProjectedFilter].
 */
val ProjectedFilter.uiLabel: String
    @Composable get() = when (this) {
        ProjectedFilter.REMAINING -> ListLabels.TAB_REMAINING
        ProjectedFilter.PAST -> ListLabels.TAB_PAID
        ProjectedFilter.ALL -> ListLabels.TAB_ALL
    }


/**
 * Returns the localized label for a [StandardFilter].
 */
val StandardFilter.uiLabel: String
    @Composable get() = when (this) {
        StandardFilter.ALL -> ListLabels.TAB_ALL
        StandardFilter.UNIQUE -> CommonLabels.CYCLE_UNIQUE
        StandardFilter.WEEKLY -> CommonLabels.CYCLE_WEEKLY
        StandardFilter.MONTHLY -> CommonLabels.CYCLE_MONTHLY
        StandardFilter.YEARLY -> CommonLabels.CYCLE_YEARLY
    }
