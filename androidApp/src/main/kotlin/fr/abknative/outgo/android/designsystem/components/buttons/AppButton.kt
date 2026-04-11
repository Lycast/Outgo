package fr.abknative.outgo.android.designsystem.components.buttons

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import fr.abknative.outgo.android.designsystem.foundation.AppTheme
import fr.abknative.outgo.android.designsystem.foundation.toColor


/**
 * The primary filled button for the application.
 * Pre-configured with the brand's primary color and typography.
 *
 * @param onClick Callback to be invoked when the button is clicked.
 * @param modifier The modifier to be applied to the button.
 * @param enabled Whether the button is enabled.
 * @param height The height of the button (default is 56.dp for standard touch targets).
 * @param shape The shape of the button corners.
 * @param content The content to be displayed inside the button.
 */
@Composable
fun AppButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    height: Dp = 48.dp,
    containerColor: Color = AppTheme.colors.primary.toColor(),
    contentColor: Color = AppTheme.colors.textOnBrand.toColor(),
    shape: Shape = AppTheme.shapes.full,
    content: @Composable RowScope.() -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(height),
        enabled = enabled,
        shape = shape,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor,
            disabledContainerColor = AppTheme.colors.surface50.toColor().copy(alpha = 0.5f),
            disabledContentColor = AppTheme.colors.textSecondary.toColor().copy(alpha = 0.5f)
        )
    ) {
        ProvideTextStyle(value = AppTheme.typo.label) {
            content()
        }
    }
}