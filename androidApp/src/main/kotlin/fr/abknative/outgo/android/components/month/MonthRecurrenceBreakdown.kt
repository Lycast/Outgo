package fr.abknative.outgo.android.components.month

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import fr.abknative.outgo.android.designsystem.foundation.AppTheme
import fr.abknative.outgo.android.designsystem.foundation.toColor
import fr.abknative.outgo.android.ui.extensions.uiAmount
import fr.abknative.outgo.wallet.api.model.operation.Recurrence

/**
 * Renders a breakdown of expenses categorized by their recurrence type.
 *
 * @param breakdown A map linking recurrence types to their total amount in cents.
 */
@Composable
fun MonthRecurrenceBreakdown(
    breakdown: Map<Recurrence, Long>
) {
    Surface(
        shape = RoundedCornerShape(AppTheme.dimens.large),
        color = AppTheme.colors.surface100.toColor(),
        shadowElevation = 2.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(AppTheme.dimens.large)) {
            Text(
                text = "Répartition",
                style = AppTheme.typo.label,
                color = AppTheme.colors.textSecondary.toColor()
            )
            Spacer(modifier = Modifier.height(AppTheme.dimens.medium))

            breakdown.forEach { (recurrence, amount) ->
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = recurrence.name,
                        style = AppTheme.typo.body
                    )
                    Text(
                        text = amount.uiAmount,
                        style = AppTheme.typo.body.copy(fontWeight = FontWeight.Medium)
                    )
                }
            }
        }
    }
}