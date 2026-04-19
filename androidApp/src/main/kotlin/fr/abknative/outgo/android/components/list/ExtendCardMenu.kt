package fr.abknative.outgo.android.components.list

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import fr.abknative.outgo.android.R
import fr.abknative.outgo.android.designsystem.components.buttons.AppIconTextButton
import fr.abknative.outgo.android.designsystem.components.buttons.AppIconTextButtonHold
import fr.abknative.outgo.android.designsystem.foundation.AppTheme
import fr.abknative.outgo.android.designsystem.foundation.toColor
import fr.abknative.outgo.android.ui.CommonLabels

@Composable
fun ExtendCardMenu(
    modifier: Modifier = Modifier,
    onEditClicked: () -> Unit,
    onDuplicateClicked: () -> Unit,
    onDeleteClicked: () -> Unit,
    onUnsubscribeClicked: (() -> Unit)? = null
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = AppTheme.dimens.large)
            .padding(bottom = AppTheme.dimens.medium, top = AppTheme.dimens.small),
        verticalArrangement = Arrangement.spacedBy(AppTheme.dimens.small)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(AppTheme.dimens.small)
        ) {
            AppIconTextButton(
                text = CommonLabels.ACTION_EDIT,
                iconRes = R.drawable.pencil_simple,
                onClick = onEditClicked,
                modifier = Modifier.weight(1f)
            )

            AppIconTextButton(
                text = CommonLabels.ACTION_DUPLICATE,
                iconRes = R.drawable.copy,
                onClick = onDuplicateClicked,
                tint = AppTheme.colors.primary.toColor(),
                modifier = Modifier.weight(1f)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(AppTheme.dimens.small)
        ) {
            if (onUnsubscribeClicked != null) {
                AppIconTextButton(
                    text = CommonLabels.ACTION_CLOSURE,
                    iconRes = R.drawable.calendar_slash,
                    onClick = onUnsubscribeClicked,
                    tint = AppTheme.colors.tertiary.toColor().copy(alpha = 0.8f),
                    modifier = Modifier.weight(1f)
                )
            }

            AppIconTextButtonHold(
                text = CommonLabels.ACTION_DELETE,
                iconRes = R.drawable.trash,
                onConfirm = onDeleteClicked,
                tint = AppTheme.colors.error.toColor().copy(alpha = 0.5f),
                modifier = Modifier.weight(1f)
            )

            if (onUnsubscribeClicked == null) { Spacer(modifier = Modifier.weight(1f)) }
        }
    }
}