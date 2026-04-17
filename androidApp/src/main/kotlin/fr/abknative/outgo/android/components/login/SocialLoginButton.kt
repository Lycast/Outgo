package fr.abknative.outgo.android.components.login

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import fr.abknative.outgo.android.R
import fr.abknative.outgo.android.designsystem.components.buttons.AppOutlinedButton
import fr.abknative.outgo.android.designsystem.foundation.AppText
import fr.abknative.outgo.android.designsystem.foundation.AppTheme
import fr.abknative.outgo.android.designsystem.foundation.toColor
import fr.abknative.outgo.android.ui.LoginLabels

enum class SocialProvider { // todo ne pas laisser cette enum dans le composant je penses
    GOOGLE, APPLE
}

@Composable
fun SocialLoginButton(
    provider: SocialProvider,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    AppOutlinedButton(
        onClick = onClick,
        color = AppTheme.colors.textPrimary.toColor(),
        modifier = modifier.fillMaxWidth()
    ) {
        val (text, iconRes) = when (provider) {
            SocialProvider.GOOGLE -> LoginLabels.GOOGLE_BUTTON to R.drawable.google_logo_bold
            SocialProvider.APPLE -> LoginLabels.APPLE_BUTTON to R.drawable.apple_logo_bold
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(painter = painterResource(id = iconRes), contentDescription = null, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(AppTheme.dimens.large))
            AppText(text = text)
        }
    }
}