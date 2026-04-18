package fr.abknative.outgo.android.components.list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import fr.abknative.outgo.android.designsystem.foundation.AppText
import fr.abknative.outgo.android.designsystem.foundation.AppTheme
import fr.abknative.outgo.android.designsystem.foundation.toColor

@Composable
fun <T : Enum<T>> FilterSelector(
    items: List<T>,
    selectedItem: T,
    onItemSelected: (T) -> Unit,
    labelMapper: @Composable (T) -> String, // Fonction pour obtenir le texte en français
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()), // Permet de scroller si trop de filtres (ex: Standard)
        horizontalArrangement = Arrangement.spacedBy(AppTheme.dimens.small),
        verticalAlignment = Alignment.CenterVertically
    ) {
        items.forEach { item ->
            val isSelected = item == selectedItem

            val backgroundColor = if (isSelected) {
                AppTheme.colors.primary.toColor()
            } else {
                AppTheme.colors.surface50.toColor().copy(alpha = 0.5f)
            }

            val textColor = if (isSelected) {
                AppTheme.colors.textOnBrand.toColor()
            } else {
                AppTheme.colors.textSecondary.toColor()
            }

            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(backgroundColor)
                    .clickable { onItemSelected(item) }
                    .padding(
                        horizontal = AppTheme.dimens.medium,
                        vertical = AppTheme.dimens.small
                    ),
                contentAlignment = Alignment.Center
            ) {
                AppText(
                    text = labelMapper(item),
                    color = textColor,
                    style = AppTheme.typo.caption.copy(
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                    )
                )
            }
        }
    }
}