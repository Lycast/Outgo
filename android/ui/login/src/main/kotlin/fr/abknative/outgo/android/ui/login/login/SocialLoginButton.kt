package fr.abknative.outgo.android.ui.login.login

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import fr.abknative.outgo.android.core.LoginLabels
import fr.abknative.outgo.android.core.R
import fr.abknative.outgo.android.core.components.buttons.AppOutlinedButton
import fr.abknative.outgo.android.core.designsystem.AppText
import fr.abknative.outgo.android.core.designsystem.AppTheme
import fr.abknative.outgo.android.core.designsystem.toColor

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