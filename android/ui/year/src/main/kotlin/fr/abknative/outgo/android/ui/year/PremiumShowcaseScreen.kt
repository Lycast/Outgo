package fr.abknative.outgo.android.ui.year

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import fr.abknative.outgo.android.core.components.buttons.AppButton
import fr.abknative.outgo.android.core.components.cards.GlassCard
import fr.abknative.outgo.android.core.designsystem.AppText
import fr.abknative.outgo.android.core.designsystem.AppTheme
import fr.abknative.outgo.android.core.designsystem.toColor

/**
 * Showcase screen for the Premium offer (Painted Door Test).
 * Displays the comparison between Essential and Premium features.
 */
@Composable
fun PremiumShowcaseScreen(
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    val uriHandler = LocalUriHandler.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(AppTheme.dimens.large),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(AppTheme.dimens.extraLarge))
        AppText(
            text = YearLabels.SHOWCASE_TITLE,
            style = AppTheme.typo.title,
            color = AppTheme.colors.primary.toColor(),
            textAlign = TextAlign.Center
        )
        AppText(
            text = YearLabels.SHOWCASE_SUBTITLE,
            color = AppTheme.colors.textSecondary.toColor(),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = AppTheme.dimens.small)
        )

        Spacer(modifier = Modifier.height(AppTheme.dimens.extraLarge))

        ShowcaseCard(
            title = YearLabels.ESSENTIAL_TITLE,
            badge = YearLabels.ESSENTIAL_BADGE,
            description = YearLabels.ESSENTIAL_DESC,
            features = YearLabels.ESSENTIAL_FEATURES,
            ending = null,
            isActive = true
        )

        Spacer(modifier = Modifier.height(AppTheme.dimens.medium))

        ShowcaseCard(
            title = YearLabels.PREMIUM_TITLE,
            badge = YearLabels.PREMIUM_BADGE,
            description = YearLabels.PREMIUM_DESC,
            features = YearLabels.PREMIUM_FEATURES,
            ending = YearLabels.PREMIUM_ENDING,
            isActive = false
        )

        Spacer(modifier = Modifier.height(AppTheme.dimens.extraLarge))

        // --- Call To Action ---
        AppButton(
            onClick = { uriHandler.openUri(YearLabels.URL_FORM) },
            modifier = Modifier.fillMaxWidth()
        ) {
            AppText(
                text = YearLabels.NOTIFY_ME_ACTION,
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
    ending: String?,
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

            if (ending != null) {
                Spacer(modifier = Modifier.height(AppTheme.dimens.medium))
                AppText(
                    text = ending,
                    modifier = Modifier.padding(top = AppTheme.dimens.small)
                )
            }
        }
    }
}