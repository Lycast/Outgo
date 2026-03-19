package fr.abknative.outgo.android.components.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fr.abknative.outgo.android.components.common.InfoTooltip
import fr.abknative.outgo.android.ui.DashboardLabels
import fr.abknative.outgo.android.ui.theme.AppTheme
import fr.abknative.outgo.android.ui.theme.OutgoTheme
import fr.abknative.outgo.android.ui.theme.toColor

@Composable
fun PairedBudgetBar(
    // --- Données du Haut ---
    topLabel: String,
    topAmount: String,
    topProgress: Float,
    topBarColor: Color,
    // --- Données du Bas ---
    bottomLabel: String,
    bottomAmount: String,
    bottomProgress: Float,
    bottomBarColor: Color,
) {
    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = AppTheme.spacing.medium),) {

        // --- 1. TEXTE DU HAUT ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                text = topLabel,
                style = AppTheme.typo.caption,
                color = AppTheme.colors.textSecondary.toColor()
            )
            Text(
                text = topAmount,
                style =AppTheme.typo.body,
                color = AppTheme.colors.textPrimary.toColor()
            )
        }

        // --- 2. LES BARRES (Sandwich) ---
        Column(
            modifier = Modifier.fillMaxWidth().padding(vertical = AppTheme.spacing.small),
            verticalArrangement = Arrangement.spacedBy(1.dp)
        ) {
            // Barre du Haut
            ProgressBarItem(
                progress = topProgress,
                color = topBarColor,
                shape = RoundedCornerShape(topStart = 2.dp, topEnd = 2.dp)
            )

            // Barre du Bas
            ProgressBarItem(
                progress = bottomProgress,
                color = bottomBarColor,
                shape = RoundedCornerShape(bottomStart = 2.dp, bottomEnd = 2.dp)
            )
        }


        InfoTooltip(
            title = DashboardLabels.TOOLTIP_BALANCE_DUE_TITLE,
            description = DashboardLabels.TOOLTIP_BALANCE_DUE_DESC,
        ) {
            // --- 3. TEXTE DU BAS ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = bottomLabel,
                    style = AppTheme.typo.caption,
                    color = AppTheme.colors.textSecondary.toColor()
                )
                Text(
                    text = bottomAmount,
                    style = AppTheme.typo.body,
                    fontWeight = FontWeight.Medium,
                    color = AppTheme.colors.textPrimary.toColor()
                )
            }
        }
    }
}


@Composable
private fun ProgressBarItem(
    progress: Float,
    color: Color,
    shape: RoundedCornerShape
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(4.dp)
            .background(
                color = AppTheme.colors.textSecondary.toColor().copy(alpha = 0.1f),
                shape = shape
            )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(progress.coerceIn(0f, 1f))
                .fillMaxHeight()
                .background(color = color, shape = shape)
        )
    }
}


@Preview(showBackground = true, name = "Paired Bar - Nominal")
@Composable
fun PreviewPairedBudgetBarNominal() {
    OutgoTheme {
        Box(modifier = Modifier.padding(16.dp).background(MaterialTheme.colorScheme.surface)) {
            PairedBudgetBar(
                topLabel = DashboardLabels.HERO_TOTAL_CHARGES_LABEL,
                topAmount = "1 250,50 €",
                topProgress = 0.7f,
                topBarColor = AppTheme.colors.secondary.toColor(),

                bottomLabel = DashboardLabels.HERO_REMAINING_TO_PAY_LABEL,
                bottomAmount = "320,00 €",
                bottomProgress = 0.25f,
                bottomBarColor =  AppTheme.colors.tertiary.toColor()
            )
        }
    }
}

@Preview(showBackground = true, name = "Paired Bar - Billionaire")
@Composable
fun PreviewPairedBudgetBarBillionaire() {
    OutgoTheme {
        Box(modifier = Modifier.padding(16.dp).background(MaterialTheme.colorScheme.surface)) {
            PairedBudgetBar(
                topLabel = DashboardLabels.HERO_TOTAL_CHARGES_LABEL,
                topAmount = "250 000 000 000,00 €",
                topProgress = 0.9f,
                topBarColor =  AppTheme.colors.secondary.toColor(),

                bottomLabel = DashboardLabels.HERO_REMAINING_TO_PAY_LABEL,
                bottomAmount = "45 000 000,00 €",
                bottomProgress = 0.1f,
                bottomBarColor =  AppTheme.colors.tertiary.toColor()
            )
        }
    }
}