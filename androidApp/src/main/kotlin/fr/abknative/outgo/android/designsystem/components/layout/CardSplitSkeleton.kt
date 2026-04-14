package fr.abknative.outgo.android.designsystem.components.layout

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import fr.abknative.outgo.android.designsystem.foundation.AppTheme

/**
 * A reusable layout skeleton that divides a card into two distinct sections.
 * * @param dataContent The textual or data-driven content, aligned to the left.
 * @param visualContent The visual content (like a chart or icon), aligned to the right.
 * @param modifier The layout modifier.
 * @param leftWeight The ratio of space the left content should take (default 1f).
 * @param rightWeight The ratio of space the right content should take (default 0.8f).
 */
@Composable
fun CardSplitSkeleton(
    dataContent: @Composable () -> Unit,
    visualContent: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    leftWeight: Float = 1f,
    rightWeight: Float = 1f
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        // Left side: Takes its ratio of the space
        Box(
            modifier = Modifier.weight(leftWeight),
            contentAlignment = Alignment.Center
        ) {
            dataContent()
        }

        Spacer(modifier = Modifier.width(AppTheme.dimens.medium))

        // Right side: Takes its ratio of the space
        Box(
            modifier = Modifier.weight(rightWeight),
            contentAlignment = Alignment.Center
        ) {
            visualContent()
        }
    }
}