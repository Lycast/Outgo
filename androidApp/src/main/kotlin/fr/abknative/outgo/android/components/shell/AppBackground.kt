package fr.abknative.outgo.android.components.shell

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import fr.abknative.outgo.android.R
import fr.abknative.outgo.android.ui.theme.AppTheme
import fr.abknative.outgo.android.ui.theme.toColor

/**
 * A reusable background container that provides a base gradient and overlays a vector pattern.
 * This structured background is required to reveal the glassmorphism effect of inner components.
 *
 * @param modifier The modifier to be applied to the layout.
 * @param content The screen content (Scaffold, LazyColumn, etc.) to be displayed on top.
 */
@Composable
fun AppBackground(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(modifier = modifier.fillMaxSize()) {

        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            AppTheme.colors.primary.toColor().copy(alpha = 0.1f),
                            AppTheme.colors.background.toColor()
                        )
                    )
                )
        )

        Image(
            painter = painterResource(id = R.drawable.parametric_lines),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.matchParentSize(),
            alpha = 0.5f
        )
        content()
    }
}