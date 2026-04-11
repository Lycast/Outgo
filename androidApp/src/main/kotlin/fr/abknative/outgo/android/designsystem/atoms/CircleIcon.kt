package fr.abknative.outgo.android.designsystem.atoms

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import fr.abknative.outgo.android.designsystem.foundation.AppTheme
import fr.abknative.outgo.android.designsystem.foundation.toColor

/**
 * A reusable circular icon container.
 * Often used in lists or headers to represent categories or actions.
 *
 * @param iconRes The drawable resource ID for the icon.
 * @param modifier The modifier to be applied to the container.
 * @param size The total size of the circular container.
 * @param iconSize The size of the icon inside the circle.
 * @param tint The color of the icon.
 * @param background The background color of the circle.
 */
@Composable
fun CircleIcon(
    iconRes: Int,
    modifier: Modifier = Modifier,
    size: Dp = 40.dp,
    iconSize: Dp = 24.dp,
    tint: Color = AppTheme.colors.primary.toColor(),
    background: Color = AppTheme.colors.textPrimary.toColor().copy(alpha = 0.05f)
) {
    Box(
        modifier = modifier
            .size(size)
            .background(color = background, shape = CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = null,
            tint = tint,
            modifier = Modifier.size(iconSize)
        )
    }
}