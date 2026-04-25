package fr.abknative.outgo.android.core.designsystem

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import fr.abknative.outgo.android.core.R

@Composable
fun AppBackground(
    modifier: Modifier = Modifier,
    isDarkMode: Boolean,
    content: @Composable () -> Unit
) {
    val primaryColor = AppTheme.colors.tertiary.toColor()
    val bgColor = AppTheme.colors.background.toColor()

    val backgroundRes = if (isDarkMode) R.drawable.bg_dark else R.drawable.bg_light

    val backgroundBrush = remember(primaryColor, bgColor) {
        Brush.verticalGradient(
            colors = listOf(
                primaryColor.copy(alpha = 0.1f),
                bgColor
            )
        )
    }

    Box(modifier = modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(brush = backgroundBrush)
        )

        Image(
            painter = painterResource(id = backgroundRes),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.matchParentSize(),
            alpha = 0.3f
        )

        content()
    }
}