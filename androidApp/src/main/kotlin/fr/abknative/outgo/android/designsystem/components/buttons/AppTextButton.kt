package fr.abknative.outgo.android.designsystem.components.buttons

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import fr.abknative.outgo.android.designsystem.foundation.AppTheme
import fr.abknative.outgo.android.designsystem.foundation.toColor

/**
 * A simple text button for less prominent actions or cancel buttons.
 */
@Composable
fun AppTextButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    height: Dp = 48.dp,
    contentColor: Color = AppTheme.colors.textSecondary.toColor(),
    content: @Composable RowScope.() -> Unit
) {
    TextButton(
        onClick = onClick,
        modifier = modifier.height(height),
        enabled = enabled,
        colors = ButtonDefaults.textButtonColors(
            contentColor = contentColor
        )
    ) {
        ProvideTextStyle(value = AppTheme.typo.label) {
            content()
        }
    }
}