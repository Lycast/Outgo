package fr.abknative.outgo.android.designsystem.components.layout

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import fr.abknative.outgo.android.designsystem.foundation.AppTheme

@Composable
fun CardSplitSkeleton(
    leftContent: @Composable () -> Unit,
    rightContent: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    leftWeight: Float = 1f,
    rightWeight: Float = 1f
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {

        Box(
            modifier = Modifier.weight(leftWeight),
            contentAlignment = Alignment.Center
        ) {
            leftContent()
        }

        Spacer(modifier = Modifier.width(AppTheme.dimens.medium))

        Box(
            modifier = Modifier.weight(rightWeight),
            contentAlignment = Alignment.Center
        ) {
            rightContent()
        }
    }
}