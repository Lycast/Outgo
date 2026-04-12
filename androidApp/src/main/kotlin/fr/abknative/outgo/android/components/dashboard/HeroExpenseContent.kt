package fr.abknative.outgo.android.components.dashboard

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import fr.abknative.outgo.android.designsystem.components.charts.AppDonutChart
import fr.abknative.outgo.android.designsystem.foundation.AppTheme
import fr.abknative.outgo.android.designsystem.foundation.toColor
import fr.abknative.outgo.android.ui.DashboardLabels
import fr.abknative.outgo.android.ui.extensions.uiAmount

/**
 * A carousel page focused on expenses visualization using a donut chart.
 * Shows the ratio between total outgoings and what's left to pay.
 */
@SuppressLint("SuspiciousIndentation")
@Composable
fun HeroExpenseContent(
    totalOutgoingsInCents: Long,
    remainingToPayInCents: Long,
) {

    // On calcule le poids du "Restant à payer" par rapport au total des charges
    val progress = if (totalOutgoingsInCents > 0) {
        // Si tu as déjà la variable remainingToPayInCents :
        remainingToPayInCents.toFloat() / totalOutgoingsInCents.toFloat()

        // OU, si tu n'as que paidAmountInCents sous la main :
        // (totalOutgoingsInCents - paidAmountInCents).toFloat() / totalOutgoingsInCents.toFloat()
    } else 0f

    Column(
        modifier = Modifier
            .height(160.dp)
            .padding(vertical = AppTheme.dimens.small),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Vue sur les dépenses",
            style = AppTheme.typo.caption,
            fontWeight = FontWeight.Medium,
            color = AppTheme.colors.textPrimary.toColor()
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = AppTheme.dimens.extraLarge)
                .padding(top = AppTheme.dimens.large),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {

            // Data Details
            Column(verticalArrangement = Arrangement.spacedBy(AppTheme.dimens.extraSmall)) {

                Text(
                    text = DashboardLabels.HERO_TOTAL_CHARGES_LABEL,
                    style = AppTheme.typo.label,
                    fontWeight = FontWeight.Medium,
                    color = AppTheme.colors.textPrimary.toColor()
                )

                Text(
                    text = totalOutgoingsInCents.uiAmount,
                    style = AppTheme.typo.title.copy(
                        fontSize = AppTheme.typo.title.fontSize * 0.8
                    ),
                    fontWeight = FontWeight.Medium,
                    color = AppTheme.colors.primary.toColor()
                )

                Spacer(modifier = Modifier.height(AppTheme.dimens.extraSmall))

                Text(
                    text = DashboardLabels.HERO_REMAINING_TO_PAY_LABEL,
                    style = AppTheme.typo.label,
                    fontWeight = FontWeight.Medium,
                    color = AppTheme.colors.textPrimary.toColor()
                )

                Text(
                    text = remainingToPayInCents.uiAmount,
                    style = AppTheme.typo.title.copy(
                        fontSize = AppTheme.typo.title.fontSize * 0.8
                    ),
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.colors.tertiary.toColor()
                )
            }

            // Visual Focus: The Donut
            AppDonutChart(
                progress = progress,
                isMirrored = true,
                activeColor = AppTheme.colors.tertiary.toColor(),
                trackColor = AppTheme.colors.primary.toColor().copy(alpha = 0.5f),
                strokeWidth = AppTheme.dimens.large,
                modifier = Modifier
                    .size(90.dp)
                    .alpha(0.8f)
            )
        }
    }
}