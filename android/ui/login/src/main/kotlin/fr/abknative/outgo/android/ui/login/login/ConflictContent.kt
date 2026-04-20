package fr.abknative.outgo.android.ui.login.login

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import fr.abknative.outgo.android.core.LoginLabels
import fr.abknative.outgo.android.core.components.buttons.AppButton
import fr.abknative.outgo.android.core.components.buttons.AppTextButton
import fr.abknative.outgo.android.core.designsystem.AppText
import fr.abknative.outgo.android.core.designsystem.AppTheme
import fr.abknative.outgo.android.core.designsystem.toColor

@Composable
fun ConflictContent(onConfirm: () -> Unit, onCancel: () -> Unit) {
    // --- Title ---
    Text(
        text = LoginLabels.CONFLICT_TITLE,
        style = AppTheme.typo.title,
        color = AppTheme.colors.primary.toColor(),
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center
    )

    Spacer(modifier = Modifier.height(AppTheme.dimens.large))

    // --- Main Explanation ---
    AppText(
        text = LoginLabels.CONFLICT_DESC,
        color = AppTheme.colors.textSecondary.toColor(),
        textAlign = TextAlign.Center
    )

    Spacer(modifier = Modifier.height(AppTheme.dimens.large))

    // --- The "Question" ---
    AppText(
        text = LoginLabels.CONFLICT_QUESTION,
        style = AppTheme.typo.caption,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth(),
    )

    Spacer(modifier = Modifier.height(AppTheme.dimens.extraLarge))

    // --- Primary Action ---
    AppButton(
        onClick = onConfirm,
        modifier = Modifier.fillMaxWidth()
    ) {
        AppText(
            text = LoginLabels.CONFLICT_CONFIRM,
            color = AppTheme.colors.textOnBrand.toColor()
        )
    }

    Spacer(modifier = Modifier.height(AppTheme.dimens.small))

    // --- Secondary Action ---
    AppTextButton(
        onClick = onCancel,
        modifier = Modifier.fillMaxWidth()
    ) {
        AppText(text = LoginLabels.CONFLICT_CANCEL)
    }
}