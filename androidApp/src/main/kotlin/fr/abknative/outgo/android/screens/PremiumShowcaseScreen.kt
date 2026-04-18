package fr.abknative.outgo.android.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import fr.abknative.outgo.android.designsystem.components.buttons.AppButton
import fr.abknative.outgo.android.designsystem.components.cards.GlassCard
import fr.abknative.outgo.android.designsystem.foundation.AppText
import fr.abknative.outgo.android.designsystem.foundation.AppTheme
import fr.abknative.outgo.android.designsystem.foundation.toColor
import fr.abknative.outgo.android.ui.PremiumLabels

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
        AppText(
            text = PremiumLabels.SHOWCASE_TITLE,
            style = AppTheme.typo.title,
            color = AppTheme.colors.primary.toColor(),
            textAlign = TextAlign.Center
        )
        AppText(
            text = PremiumLabels.SHOWCASE_SUBTITLE,
            color = AppTheme.colors.textSecondary.toColor(),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = AppTheme.dimens.small)
        )

        Spacer(modifier = Modifier.height(AppTheme.dimens.extraLarge))

        // --- Carte Offre Actuelle (Essentiel) ---
        ShowcaseCard(
            title = PremiumLabels.ESSENTIAL_TITLE,
            badge = PremiumLabels.ESSENTIAL_BADGE,
            description = PremiumLabels.ESSENTIAL_DESC,
            features = PremiumLabels.ESSENTIAL_FEATURES,
            isActive = true
        )

        Spacer(modifier = Modifier.height(AppTheme.dimens.medium))

        // --- Carte Futur (Premium) ---
        ShowcaseCard(
            title = PremiumLabels.PREMIUM_TITLE,
            badge = PremiumLabels.PREMIUM_BADGE,
            description = PremiumLabels.PREMIUM_DESC,
            features = PremiumLabels.PREMIUM_FEATURES,
            isActive = false
        )

        Spacer(modifier = Modifier.height(AppTheme.dimens.extraLarge))

        // --- Call To Action ---
        AppButton(
            onClick = onNotifyMeClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            AppText(
                text = PremiumLabels.NOTIFY_ME_ACTION,
                color = AppTheme.colors.textOnBrand.toColor()
            )
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
            AppText(
                text = badge,
                color = if (isActive) AppTheme.colors.primary.toColor() else AppTheme.colors.secondary.toColor(),
            )
            AppText(
                text = title,
                style = AppTheme.typo.title,
                modifier = Modifier.padding(top = AppTheme.dimens.extraSmall)
            )
            AppText(
                text = description,
                modifier = Modifier.padding(top = AppTheme.dimens.small)
            )

            Spacer(modifier = Modifier.height(AppTheme.dimens.medium))

            features.forEach { feature ->
                AppText(
                    text = "• $feature",
                    color = AppTheme.colors.textSecondary.toColor(),
                    modifier = Modifier.padding(vertical = 2.dp)
                )
            }
        }
    }
}