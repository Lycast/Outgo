package fr.abknative.outgo.android.components.dashboard

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import fr.abknative.outgo.android.designsystem.foundation.AppTheme
import fr.abknative.outgo.android.designsystem.foundation.toColor

/**
 * A reusable summary item for the Hero Section.
 * Handles the display of a raw icon, a descriptive label, and a formatted amount.
 *
 * @param iconRes The drawable resource for the icon.
 * @param label The descriptive text (e.g., "Disposable Income").
 * @param amount The formatted currency string.
 * @param iconTint The color of the icon.
 * @param amountColor The color of the amount text.
 * @param onClick Optional click action for the item.
 * @param fontWeight Weight for the amount text.
 */
@Composable
fun HeroStatItem(
    iconRes: Int,
    label: String,
    amount: String,
    iconTint: Color,
    amountColor: Color = AppTheme.colors.textPrimary.toColor(),
    onClick: (() -> Unit)? = null,
    fontWeight: FontWeight = FontWeight.Bold
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(AppTheme.dimens.medium),
        verticalAlignment = Alignment.CenterVertically,
        modifier = if (onClick != null) {
            Modifier.clickable(
                role = Role.Button,
                onClick = onClick
            )
        } else Modifier
    ) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = null,
            tint = iconTint,
            modifier = Modifier.padding(AppTheme.dimens.small).size(32.dp)
        )

        Column(horizontalAlignment = Alignment.Start) {
            Text(
                text = label,
                style = AppTheme.typo.label,
                color = AppTheme.colors.textSecondary.toColor(),
                fontWeight = FontWeight.Medium
            )

            Text(
                text = amount,
                style = AppTheme.typo.title.copy(
                    fontSize = AppTheme.typo.title.fontSize * 0.8
                ),
                color = amountColor,
                fontWeight = fontWeight
            )
        }
    }
}