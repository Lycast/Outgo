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
import androidx.compose.ui.unit.dp
import fr.abknative.outgo.android.components.common.InfoTooltip
import fr.abknative.outgo.android.ui.DashboardLabels

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
    Column(modifier = Modifier.fillMaxWidth()) {

        // --- 1. TEXTE DU HAUT ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                text = topLabel,
                style = MaterialTheme.typography.bodySmall,
                color =  MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = topAmount,
                color =  MaterialTheme.colorScheme.onSurface
            )
        }

        // --- 2. LES BARRES (Sandwich) ---
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(2.dp)
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
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = bottomAmount,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
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
            .height(8.dp)
            .background(
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
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