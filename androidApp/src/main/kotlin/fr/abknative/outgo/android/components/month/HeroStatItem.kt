package fr.abknative.outgo.android.components.month

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fr.abknative.outgo.android.designsystem.foundation.AppTheme
import fr.abknative.outgo.android.designsystem.foundation.OutgoTheme
import fr.abknative.outgo.android.designsystem.foundation.toColor

/**
 * A reusable summary item for the Hero Section.
 * Handles the display of a raw icon, a descriptive label, and a formatted amount.
 *
 * @param iconRes The drawable resource for the icon.
 * @param label The descriptive text (e.g., "Disposable Income").
 * @param amount The formatted currency string.
 * @param liveColor The dynamic color.
 * @param amountColor The color of the amount text.
 * @param onClick Optional click action for the item.
 * @param fontWeight Weight for the amount text.
 */
@Composable
fun HeroStatItem(
    iconRes: Int,
    amount: String,
    liveColor: Color,
    amountColor: Color = AppTheme.colors.textPrimary.toColor(),
    onClick: (() -> Unit)? = null,
    fontWeight: FontWeight = FontWeight.Bold,
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
            tint = liveColor,
            modifier = Modifier
                .padding(AppTheme.dimens.small)
                .size(32.dp)
        )

        Text(
            text = amount,
            style = AppTheme.typo.title.copy(
                fontSize = AppTheme.typo.title.fontSize * 0.8
            ),
            color = liveColor,
            fontWeight = fontWeight
        )
    }
}

/**
 * Preview demonstrating how HeroStatItems look when placed side-by-side
 * in a Row, similar to the main Budget card.
 */
@Preview(showBackground = true, backgroundColor = 0xFFF0F4F8, name = "HeroStatItem - Row Layout")
@Composable
fun PreviewHeroStatItemRow() {
    OutgoTheme {
        Surface(
            modifier = Modifier.padding(16.dp),
            shape = androidx.compose.foundation.shape.RoundedCornerShape(AppTheme.dimens.large),
            shadowElevation = 4.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(AppTheme.dimens.large),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                HeroStatItem(
                    iconRes = android.R.drawable.ic_menu_myplaces, // Remplacer par ton icône Banque

                    amount = "200,00 €",
                    liveColor = AppTheme.colors.primary.toColor()
                )

                HeroStatItem(
                    iconRes = android.R.drawable.ic_menu_agenda, // Remplacer par ton icône Tirelire

                    amount = "80,00 €",
                    // Couleur jaune/or statique pour coller à ton design actuel
                    liveColor = Color(0xFFD4AF37)
                )
            }
        }
    }
}