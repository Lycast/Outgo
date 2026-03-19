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
import fr.abknative.outgo.android.ui.theme.AppTheme
import fr.abknative.outgo.android.ui.theme.toColor
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
                    color = AppTheme.colors.textSecondary.toColor().copy(alpha = 0.2f),
                    shape = tooltipShape
                ),
                shape = tooltipShape,
                colors = TooltipDefaults.richTooltipColors(
                    containerColor = AppTheme.colors.surface200.toColor(),
                    titleContentColor = AppTheme.colors.textPrimary.toColor(),
                    contentColor = AppTheme.colors.textSecondary.toColor(),
                    actionContentColor = AppTheme.colors.primary.toColor()
                ),
                title = {
                    Text(
                        text = title,
                        style = AppTheme.typo.body,
                        color = AppTheme.colors.textPrimary.toColor()
                    )
                },
                text = {
                    Text(
                        text = description,
                        style = AppTheme.typo.caption,
                        color = AppTheme.colors.textSecondary.toColor()
                    )
                },
                action = {
                    OutlinedButton(
                        onClick = { scope.launch { tooltipState.dismiss() } },
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = AppTheme.colors.primary.toColor()
                        )
                    ) {
                        Text(
                            text = CommonLabels.ACTION_CLOSE,
                            style = AppTheme.typo.caption
                        )
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