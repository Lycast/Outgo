package fr.abknative.outgo.android.components.shell

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import fr.abknative.outgo.android.designsystem.components.cards.GlassCard
import fr.abknative.outgo.android.designsystem.foundation.AppText
import fr.abknative.outgo.android.designsystem.foundation.AppTheme
import fr.abknative.outgo.android.designsystem.foundation.toColor
import fr.abknative.outgo.android.ui.LoginLabels

// TODO Nettoyer le code de ce composant
@Composable
fun LoadingOverlay() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.6f))
            .padding(AppTheme.dimens.large),
        contentAlignment = Alignment.Center
    ) {
        GlassCard {
            Column(
                modifier = Modifier.padding(AppTheme.dimens.extraLarge),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator(color = AppTheme.colors.primary.toColor())
                Spacer(modifier = Modifier.height(AppTheme.dimens.large))
                AppText(
                    text = LoginLabels.POST_LOGIN_SYNC_TITLE,
                    style = AppTheme.typo.subtitle,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(AppTheme.dimens.medium))
                AppText(
                    text = LoginLabels.POST_LOGIN_SYNC_MESSAGE,
                    color = AppTheme.colors.textSecondary.toColor(),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}