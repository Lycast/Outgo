package fr.abknative.outgo.android.components.month

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import fr.abknative.outgo.android.designsystem.foundation.AppText
import fr.abknative.outgo.android.designsystem.foundation.AppTheme

@Composable
fun HeroStatItem(
    iconRes: Int,
    amount: String,
    liveColor: Color,
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

        AppText(
            text = amount,
            style = AppTheme.typo.subtitle.copy(fontWeight = fontWeight),
            color = liveColor,
        )
    }
}