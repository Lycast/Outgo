package fr.abknative.outgo.android.designsystem.components.selection

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import fr.abknative.outgo.android.R
import fr.abknative.outgo.android.designsystem.foundation.AppTheme
import fr.abknative.outgo.android.designsystem.foundation.toColor

/**
 * A wrapper component for HorizontalPager that adds navigation arrows (overlay)
 * and a page indicator at the bottom.
 *
 * @param pagerState The state of the wrapped HorizontalPager.
 * @param actualPageCount The real number of items in the list, regardless of virtual/infinite paging.
 * @param onLeftClick Callback for the left navigation arrow.
 * @param onRightClick Callback for the right navigation arrow.
 */
@Composable
fun AppPagerContainer(
    pagerState: PagerState,
    actualPageCount: Int,
    onLeftClick: () -> Unit,
    onRightClick: () -> Unit,
    modifier: Modifier = Modifier,
    showArrows: Boolean = true,
    showIndicator: Boolean = true,
    content: @Composable () -> Unit
) {
    Box(modifier = modifier.fillMaxWidth()) {
        content()

        // --- NAVIGATION ARROWS ---
        if (showArrows && actualPageCount > 1) {
            PagerArrow(
                iconRes = R.drawable.caret_left,
                modifier = Modifier.align(Alignment.CenterStart),
                onClick = onLeftClick
            )

            PagerArrow(
                iconRes = R.drawable.caret_right,
                modifier = Modifier.align(Alignment.CenterEnd),
                onClick = onRightClick
            )
        }

        // --- PAGE INDICATOR ---
        if (showIndicator && actualPageCount > 1) {
            Row(
                Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = AppTheme.dimens.small),
                horizontalArrangement = Arrangement.spacedBy(AppTheme.dimens.small)
            ) {
                repeat(actualPageCount) { iteration ->
                    val isSelected = (pagerState.currentPage % actualPageCount) == iteration

                    val color = if (isSelected) {
                        AppTheme.colors.primary.toColor()
                    } else {
                        AppTheme.colors.textSecondary.toColor().copy(alpha = 0.3f)
                    }
                    Box(
                        modifier = Modifier
                            .padding(AppTheme.dimens.extraSmall)
                            .clip(CircleShape)
                            .background(color)
                            .size(6.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun PagerArrow(
    iconRes: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .minimumInteractiveComponentSize()
            .clip(CircleShape)
            .clickable(role = Role.Button, onClick = onClick)
            .padding(AppTheme.dimens.small),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            tint = AppTheme.colors.textSecondary.toColor().copy(alpha = 0.4f),
            modifier = Modifier.size(20.dp)
        )
    }
}