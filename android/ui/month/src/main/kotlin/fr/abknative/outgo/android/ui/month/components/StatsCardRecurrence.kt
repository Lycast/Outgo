package fr.abknative.outgo.android.ui.month.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import fr.abknative.outgo.android.core.components.cards.GlassCard
import fr.abknative.outgo.android.core.components.charts.AppProgressBar
import fr.abknative.outgo.android.core.designsystem.AppText
import fr.abknative.outgo.android.core.designsystem.AppTheme
import fr.abknative.outgo.android.core.designsystem.toColor
import fr.abknative.outgo.android.core.extensions.uiAmount
import fr.abknative.outgo.android.ui.month.MonthLabels
import fr.abknative.outgo.android.ui.month.getUiColor
import fr.abknative.outgo.android.ui.month.uiLabel
import fr.abknative.outgo.month.api.RecurrenceStat
import fr.abknative.outgo.wallet.api.model.operation.Recurrence

@Composable
fun StatsCardRecurrence(
    modifier: Modifier = Modifier,
    breakdown: Map<Recurrence, RecurrenceStat>
) {

    if (breakdown.isEmpty()) return

    Column(modifier = modifier) {
        AppText(
            text = MonthLabels.SECTION_RECURRENCE.uppercase(),
            style = AppTheme.typo.label.copy(fontWeight = FontWeight.Bold),
            color = AppTheme.colors.primary.toColor(),
            modifier = Modifier.padding(
                bottom = AppTheme.dimens.small,
                start = AppTheme.dimens.medium
            )
        )
        GlassCard {
            Column(
                modifier = Modifier.padding(
                    horizontal = AppTheme.dimens.extraLarge,
                    vertical = AppTheme.dimens.large
                )
            ) {
                breakdown.forEach { (recurrence, stat) ->
                    Column(
                        modifier = Modifier.padding(vertical = AppTheme.dimens.small)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            AppText(
                                text = recurrence.uiLabel,
                                style = AppTheme.typo.caption,
                            )
                            AppText(
                                text = stat.amountInCents.uiAmount,
                                style = AppTheme.typo.subtitle.copy(fontWeight = FontWeight.Medium),
                            )
                        }

                        AppProgressBar(
                            progress = stat.progress,
                            activeColor = recurrence.getUiColor().copy(alpha = 0.5f),
                            isVertical = false,
                            modifier = Modifier.fillMaxWidth(),
                            thickness = AppTheme.dimens.medium,
                        )
                    }
                }
            }
        }
    }
}
