package fr.abknative.outgo.android.designsystem.components.buttons

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import fr.abknative.outgo.android.designsystem.components.cards.GlassCard

@Composable
fun AppHeaderButton(
    onClick: () -> Unit,
    enabled: Boolean = true,
    elevation: Dp = 12.dp,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {

    GlassCard(
        modifier = modifier.size(42.dp),
        elevation = elevation

    ) {
        IconButton(
            onClick = onClick,
            enabled = enabled,
            modifier = Modifier.size(42.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                content()
            }
        }
    }
}
