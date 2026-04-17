package fr.abknative.outgo.android.components.sync

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import fr.abknative.outgo.android.designsystem.foundation.AppTheme
import fr.abknative.outgo.android.designsystem.foundation.toColor

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
        Card(
            colors = CardDefaults.cardColors(containerColor = AppTheme.colors.background.toColor()),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(AppTheme.dimens.extraLarge),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator(color = AppTheme.colors.primary.toColor())
                Spacer(modifier = Modifier.height(AppTheme.dimens.large))
                Text(
                    text = "Recherche de vos données...",
                    style = AppTheme.typo.subtitle,
                    fontWeight = FontWeight.Medium,
                    color = AppTheme.colors.textPrimary.toColor(),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(AppTheme.dimens.small))
                Text(
                    text = "Veuillez patienter quelques instants.",
                    style = AppTheme.typo.body,
                    color = AppTheme.colors.textSecondary.toColor(),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}