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
import fr.abknative.outgo.month.api.RecurrenceStat
import fr.abknative.outgo.wallet.api.model.operation.Recurrence

@Composable
fun StatsCardRecurrence(
    breakdown: Map<Recurrence, RecurrenceStat>,
) {

    Column(modifier = Modifier.padding(horizontal = AppTheme.dimens.extraLarge, vertical = AppTheme.dimens.large)) {

        breakdown.forEach { (recurrence, stat) ->

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
                        text = stat.amountInCents.uiAmount,
                        style = AppTheme.typo.body,
                        color = AppTheme.colors.textPrimary.toColor()
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
