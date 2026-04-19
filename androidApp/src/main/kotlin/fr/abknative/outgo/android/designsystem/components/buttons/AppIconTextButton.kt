package fr.abknative.outgo.android.designsystem.components.buttons

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import fr.abknative.outgo.android.designsystem.foundation.AppText
import fr.abknative.outgo.android.designsystem.foundation.AppTheme
import fr.abknative.outgo.android.designsystem.foundation.toColor

@Composable
fun AppIconTextButton(
    text: String,
    iconRes: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    tint: Color = AppTheme.colors.textPrimary.toColor()
) {
    Row(
        modifier = modifier
            .height(AppTheme.dimens.big)
            .clip(AppTheme.shapes.medium)
            .background(color = AppTheme.colors.textSecondary.toColor().copy(alpha = 0.05f))
            .clickable(onClick = onClick)
            .padding(horizontal = AppTheme.dimens.large, vertical = AppTheme.dimens.small),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            tint = tint,
            modifier = Modifier.size(20.dp)
        )

        Spacer(modifier = Modifier.width(AppTheme.dimens.small))

        AppText(
            text = text,
            style = AppTheme.typo.label,
            color = tint
        )
    }
}