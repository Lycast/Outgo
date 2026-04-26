package fr.abknative.outgo.android.ui.month.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import fr.abknative.outgo.android.core.components.cards.GlassCard
import fr.abknative.outgo.android.core.components.cards.OperationCard
import fr.abknative.outgo.android.core.designsystem.AppText
import fr.abknative.outgo.android.core.designsystem.AppTheme
import fr.abknative.outgo.android.core.designsystem.toColor
import fr.abknative.outgo.android.core.extensions.uiAmount
import fr.abknative.outgo.android.ui.month.MonthLabels
import fr.abknative.outgo.android.ui.month.getUiColor
import fr.abknative.outgo.android.ui.month.uiLabel
import fr.abknative.outgo.wallet.api.model.presenter.ProjectedOperation
import java.util.Locale.getDefault

/**
 * A dedicated section for the Month screen that displays a preview of upcoming operations.
 *
 * @param nextUpcomingExpenses The list of projected occurrences to display.
 * @param onNavigateToList Callback triggered when the card is clicked to view the full list.
 */
@Composable
fun CardUpcomingExpenses(
    nextUpcomingExpenses: List<ProjectedOperation>,
    onNavigateToList: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (nextUpcomingExpenses.isEmpty()) return

    Column(modifier = modifier) {
        AppText(
            text = MonthLabels.SECTION_UPCOMING.uppercase(getDefault()),
            style = AppTheme.typo.label.copy(fontWeight = FontWeight.Bold),
            color = AppTheme.colors.primary.toColor(),
            modifier = Modifier.padding(
                bottom = AppTheme.dimens.small,
                start = AppTheme.dimens.medium
            )
        )
        GlassCard {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onNavigateToList)
            ) {
                nextUpcomingExpenses.forEachIndexed { index, projectedOp ->
                    val displayOperation = projectedOp.operation.copy(
                        startDate = projectedOp.projectedDate
                    )

                    OperationCard(
                        topLeftText = displayOperation.name,
                        topRightText = displayOperation.recurrence.uiLabel,
                        bottomLeftText = "${MonthLabels.DUE_PREFIX} ${projectedOp.formattedStartDate}",
                        bottomRightText = displayOperation.amountInCents.uiAmount,
                        iconColor = displayOperation.recurrence.getUiColor().copy(alpha = 0.7f),
                    )

                    if (index < nextUpcomingExpenses.lastIndex) {
                        HorizontalDivider(color = AppTheme.colors.surface100.toColor())
                    }
                }
            }
        }
    }
}