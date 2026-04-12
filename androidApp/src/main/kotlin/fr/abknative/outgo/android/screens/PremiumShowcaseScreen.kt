package fr.abknative.outgo.android.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import fr.abknative.outgo.android.designsystem.components.buttons.AppButton
import fr.abknative.outgo.android.designsystem.components.cards.GlassCard
import fr.abknative.outgo.android.designsystem.foundation.AppTheme
import fr.abknative.outgo.android.designsystem.foundation.toColor

/**
 * Showcase screen for the Premium offer (Painted Door Test).
 * Displays the comparison between Essential and Premium features.
 */
@Composable
fun PremiumShowcaseScreen(
    onNotifyMeClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(AppTheme.dimens.large),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // --- Header (Hook Visuel) ---
        Spacer(modifier = Modifier.height(AppTheme.dimens.extraLarge))
        Text(
            text = "Prenez le contrôle total de votre avenir financier.",
            style = AppTheme.typo.title,
            color = AppTheme.colors.primary.toColor(),
            textAlign = TextAlign.Center
        )
        Text(
            text = "Passez d'une gestion réactive à une gestion prédictive.",
            style = AppTheme.typo.body,
            color = AppTheme.colors.textSecondary.toColor(),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = AppTheme.dimens.small)
        )

        Spacer(modifier = Modifier.height(AppTheme.dimens.extraLarge))

        // --- Carte Offre Actuelle (Essentiel) ---
        ShowcaseCard(
            title = "Outgo Essentiel",
            badge = "VOTRE OFFRE ACTUELLE",
            description = "Le contrôle de votre mois en cours.",
            features = listOf(
                "Enveloppe mensuelle simple",
                "Suivi instantané des dépenses",
                "Jauge de survie en temps réel"
            ),
            isActive = true
        )

        Spacer(modifier = Modifier.height(AppTheme.dimens.medium))

        // --- Carte Futur (Premium) ---
        ShowcaseCard(
            title = "Outgo Premium",
            badge = "BIENTÔT DISPONIBLE",
            description = "Le pilotage de votre trésorerie à long terme.",
            features = listOf(
                "💰 Véritable Gestion de Trésorerie",
                "📂 Portefeuilles Multiples",
                "🔭 Projections sur 12 Mois",
                "💳 Synchro Bancaire (V3 ?)"
            ),
            isActive = false
        )

        Spacer(modifier = Modifier.height(AppTheme.dimens.extraLarge))

        // --- Call To Action ---
        AppButton(
            onClick = onNotifyMeClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Prévenez-moi du lancement")
        }

        Spacer(modifier = Modifier.height(80.dp))
    }
}

@Composable
private fun ShowcaseCard(
    title: String,
    badge: String,
    description: String,
    features: List<String>,
    isActive: Boolean
) {
    GlassCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(AppTheme.dimens.large)) {
            Text(
                text = badge,
                style = AppTheme.typo.caption,
                color = if (isActive) AppTheme.colors.primary.toColor() else AppTheme.colors.secondary.toColor(),
                fontWeight = FontWeight.Bold
            )
            Text(
                text = title,
                style = AppTheme.typo.title,
                modifier = Modifier.padding(top = AppTheme.dimens.extraSmall)
            )
            Text(
                text = description,
                style = AppTheme.typo.body,
                color = AppTheme.colors.textPrimary.toColor(),
                modifier = Modifier.padding(top = AppTheme.dimens.small)
            )

            Spacer(modifier = Modifier.height(AppTheme.dimens.medium))

            features.forEach { feature ->
                Text(
                    text = "• $feature",
                    style = AppTheme.typo.body,
                    color = AppTheme.colors.textSecondary.toColor(),
                    modifier = Modifier.padding(vertical = 2.dp)
                )
            }
        }
    }
}