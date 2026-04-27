package fr.abknative.outgo.android.app.shell.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import fr.abknative.outgo.android.app.shell.ShellLabels
import fr.abknative.outgo.android.core.R
import fr.abknative.outgo.android.core.components.buttons.AppIconTextButton
import fr.abknative.outgo.android.core.designsystem.AppText
import fr.abknative.outgo.android.core.designsystem.AppTheme
import fr.abknative.outgo.android.core.designsystem.toColor

@Composable
fun EmailVerificationBanner(
    onCheckVerificationClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(AppTheme.colors.surface200.toColor())
            .padding(horizontal = AppTheme.dimens.large, vertical = AppTheme.dimens.medium),
        verticalAlignment = Alignment.CenterVertically
    ) {

        AppText(
            text = ShellLabels.EMAIL_VERIFICATION_MESSAGE,
            style = AppTheme.typo.label,
            modifier = Modifier.weight(1f)
        )

        AppIconTextButton(
            text =  ShellLabels.EMAIL_VERIFICATION_ACTION,
            iconRes = R.drawable.envelope_open,
            onClick = onCheckVerificationClick,
            tint = AppTheme.colors.tertiary.toColor()
        )
    }
    Spacer(Modifier.height(AppTheme.dimens.small))
}