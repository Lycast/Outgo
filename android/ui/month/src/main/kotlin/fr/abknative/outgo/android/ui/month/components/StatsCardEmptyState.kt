package fr.abknative.outgo.android.ui.month.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import fr.abknative.outgo.android.core.components.buttons.BottomCentralAction
import fr.abknative.outgo.android.core.components.cards.GlassCard
import fr.abknative.outgo.android.core.designsystem.AppText
import fr.abknative.outgo.android.core.designsystem.AppTheme
import fr.abknative.outgo.android.core.designsystem.toColor
import fr.abknative.outgo.android.ui.month.MonthLabels

@Composable
fun StatsCardEmptyState(
    onAddOperationClick: () -> Unit,
) {

    GlassCard {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(AppTheme.dimens.large),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(AppTheme.dimens.medium)
        ) {

            AppText(
                text = MonthLabels.EMPTY_STATE_TITLE,
                style = AppTheme.typo.subtitle
            )

            AppText(
                text = MonthLabels.EMPTY_STATE_DESC,
                style = AppTheme.typo.caption,
                color = AppTheme.colors.textSecondary.toColor(),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(AppTheme.dimens.medium))

            Box(
                modifier = Modifier
                    .clickable(onClick = onAddOperationClick)
                    .shadow(8.dp, CircleShape)
                    .clip(CircleShape)
                    .background(AppTheme.colors.surface50.toColor())

            ) {
                BottomCentralAction(
                    modifier = Modifier.padding(AppTheme.dimens.medium)
                )
            }
        }
    }
}
