package fr.abknative.outgo.android.components.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fr.abknative.outgo.android.R
import fr.abknative.outgo.android.ui.AccessibilityLabels
import fr.abknative.outgo.android.ui.theme.AppTheme
import fr.abknative.outgo.android.ui.theme.OutgoTheme
import fr.abknative.outgo.android.ui.theme.toColor

/**
 * A custom action trigger designed with a multi-layer approach.
 * Uses an isolation layer for clean shadow rendering and a top layer
 * for the primary color and border lighting.
 *
 * @param onClick Callback to be invoked when the button is pressed.
 * @param modifier Modifier to be applied to the outer container.
 */
@Composable
fun AddActionTrigger(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val outerShape = RoundedCornerShape(AppTheme.spacing.large)
    val innerShape = RoundedCornerShape(AppTheme.spacing.medium)

    Box(
        modifier = modifier.size(58.dp),
        contentAlignment = Alignment.Center
    ) {
        // Layer 1: Isolation & Shadow
        Box(
            modifier = Modifier
                .matchParentSize()
                .graphicsLayer {
                    shape = outerShape
                    shadowElevation = 12f
                }
                .background(color = AppTheme.colors.background.toColor(), shape = outerShape)
        )

        // Layer 2: La Bordure (Le cadre)
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            AppTheme.colors.textSecondary.toColor().copy(alpha = 0.1f),
                            AppTheme.colors.textSecondary.toColor().copy(alpha = 0.15f)
                        )
                    ),
                    shape = outerShape
                )
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(AppTheme.spacing.medium)
                .clip(innerShape)
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            AppTheme.colors.primary.toColor(),
                            AppTheme.colors.primary.toColor().copy(alpha = 0.9f)
                        )
                    )
                )
                .clickable(onClick = onClick),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(R.drawable.plus_bold),
                contentDescription = AccessibilityLabels.ADD_EXPENSE,
                tint = AppTheme.colors.textOnBrand.toColor(),
                modifier = Modifier.size(24.dp)
            )
        }
    }
}


/**
 * Preview for the AddActionTrigger component.
 * Wrapped in [AppTheme] to provide the required CompositionLocal color tokens.
 */
@Preview(showBackground = true, name = "AddActionTrigger - Glass Effect")
@Composable
fun PreviewAddActionTrigger() {
    OutgoTheme {
        Box(
            modifier = Modifier
                .background(AppTheme.colors.background.toColor())
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            AddActionTrigger(
                onClick = { /* Preview mock action */ }
            )
        }
    }
}