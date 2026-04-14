package fr.abknative.outgo.android.components.month

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import fr.abknative.outgo.android.designsystem.components.charts.AppProgressBar
import fr.abknative.outgo.android.designsystem.foundation.AppTheme
import fr.abknative.outgo.android.designsystem.foundation.toColor
import fr.abknative.outgo.android.ui.extensions.getUiColor
import fr.abknative.outgo.android.ui.extensions.uiAmount
import fr.abknative.outgo.android.ui.extensions.uiLabel
import fr.abknative.outgo.wallet.api.model.operation.Recurrence

/**
 * Renders a breakdown of expenses categorized by their recurrence type with progress bars.
 *
 * @param breakdown A map linking recurrence types to their total amount in cents.
 * @param monthlyIncome The total income reference to calculate percentages.
 */
@Composable
fun StatsCardRecurrence(
    breakdown: Map<Recurrence, Long>,
    monthlyIncome: Long
) {

    Column(modifier = Modifier.padding(horizontal = AppTheme.dimens.extraLarge, vertical = AppTheme.dimens.large)) {

        breakdown.forEach { (recurrence, amount) ->
            // Calculate progress safely
            val progress = if (monthlyIncome > 0) {
                amount.toFloat() / monthlyIncome.toFloat()
            } else 0f

            Column(
                modifier = Modifier.padding(vertical = AppTheme.dimens.small)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = recurrence.uiLabel,
                        style = AppTheme.typo.caption,
                    )
                    Text(
                        text = amount.uiAmount,
                        style = AppTheme.typo.body,
                        color = AppTheme.colors.textPrimary.toColor()
                    )
                }

                // Using the progress bar created earlier
                AppProgressBar(
                    progress = progress,
                    activeColor = recurrence.getUiColor().copy(alpha = 0.5f),
                    isVertical = false,
                    modifier = Modifier.fillMaxWidth(),
                    thickness = AppTheme.dimens.medium,
                )
            }
        }
    }
}
