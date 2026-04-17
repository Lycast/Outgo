package fr.abknative.outgo.android.components.shell

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import fr.abknative.outgo.android.designsystem.foundation.AppText
import fr.abknative.outgo.android.designsystem.foundation.AppTheme
import fr.abknative.outgo.android.designsystem.foundation.toColor
import fr.abknative.outgo.android.ui.CommonLabels
import fr.abknative.outgo.sync.api.model.SyncState

@Composable
fun Header(
    modifier: Modifier = Modifier,
    syncState: SyncState,
    isVertical: Boolean = false,
    onSyncIconClick: () -> Unit,
) {
    val containerModifier = if (isVertical) {
        modifier
            .fillMaxHeight()
            .width(IntrinsicSize.Min)
            .padding(vertical = AppTheme.dimens.large)
    } else {
        modifier
            .fillMaxWidth()
            .padding(top = AppTheme.dimens.big)
            .padding(bottom = AppTheme.dimens.medium)
            .padding(horizontal = AppTheme.dimens.large)
    }

    // --- ACTIONS CONTENT (Sync + Settings) ---
    val actionsContent = @Composable {
        SyncIconLogic(
            syncState = syncState,
            onClick = onSyncIconClick
        )
    }

    // --- RENDER ---
    if (isVertical) {
        Column(
            modifier = containerModifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            AppText(
                text = CommonLabels.APP_NAME,
                style = AppTheme.typo.title,
                color = AppTheme.colors.primary.toColor(),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = AppTheme.dimens.medium)
                    .padding(top = AppTheme.dimens.medium)
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(AppTheme.dimens.medium)
            ) {
                actionsContent()
            }
        }
    } else {
        Row(
            modifier = containerModifier,
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            AppText(
                text = CommonLabels.APP_NAME,
                style = AppTheme.typo.title,
                color = AppTheme.colors.primary.toColor()
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(AppTheme.dimens.medium)
            ) {
                actionsContent()
            }
        }
    }
}