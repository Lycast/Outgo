package fr.abknative.outgo.android.components.common

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fr.abknative.outgo.android.ui.CommonLabels
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InfoTooltip(
    title: String,
    description: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val tooltipState = rememberTooltipState(isPersistent = true)
    val scope = rememberCoroutineScope()

    val tooltipShape = MaterialTheme.shapes.medium

    TooltipBox(
        positionProvider = TooltipDefaults.rememberRichTooltipPositionProvider(),
        tooltip = {
            RichTooltip(
                modifier = Modifier.border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.2f),
                    shape = tooltipShape
                ),
                shape = tooltipShape,
                colors = TooltipDefaults.richTooltipColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    actionContentColor = MaterialTheme.colorScheme.primary
                ),
                title = { Text(text = title) },
                text = { Text(text = description) },
                action = {
                    OutlinedButton(
                        onClick = { scope.launch { tooltipState.dismiss() } }
                    ) {
                        Text(text = CommonLabels.ACTION_CLOSE)
                    }
                }
            )
        },
        state = tooltipState,
        modifier = modifier,
    ) {
        Box(
            modifier = Modifier.clickable {
                scope.launch { tooltipState.show() }
            }
        ) {
            content()
        }
    }
}