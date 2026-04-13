package fr.abknative.outgo.android.components.month

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import fr.abknative.outgo.android.designsystem.foundation.AppTheme
import fr.abknative.outgo.android.designsystem.foundation.toColor
import fr.abknative.outgo.android.ui.extensions.uiAmount

/**
 * Displays the high-level budget overview for the selected month.
 *
 * @param income The total monthly income in cents.
 * @param remaining The disposable income left in cents.
 */
@Composable
fun MonthBudgetSummaryCard(
    income: Long,
    remaining: Long
) {
    Surface(
        shape = RoundedCornerShape(AppTheme.dimens.large),
        color = AppTheme.colors.surface100.toColor(),
        shadowElevation = 2.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(AppTheme.dimens.large),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Budget",
                    color = AppTheme.colors.textSecondary.toColor(),
                    style = AppTheme.typo.body
                )
                Text(
                    text = income.uiAmount,
                    color = AppTheme.colors.primary.toColor(),
                    style = AppTheme.typo.title.copy(fontWeight = FontWeight.Bold)
                )

                Spacer(modifier = Modifier.height(AppTheme.dimens.small))

                Text(
                    text = "Restant",
                    color = AppTheme.colors.textSecondary.toColor(),
                    style = AppTheme.typo.body
                )
                Text(
                    text = remaining.uiAmount,
                    color = AppTheme.colors.tertiary.toColor(),
                    style = AppTheme.typo.title.copy(fontWeight = FontWeight.Bold)
                )
            }

            // Placeholder for the AppDonutChart
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(AppTheme.colors.primary.toColor().copy(alpha = 0.2f))
            )
        }
    }
}