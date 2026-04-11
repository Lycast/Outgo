package fr.abknative.outgo.android.components.dashboard

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.dp
import fr.abknative.outgo.android.R
import fr.abknative.outgo.android.designsystem.foundation.AppTheme
import fr.abknative.outgo.android.designsystem.foundation.toColor
import fr.abknative.outgo.android.ui.AccessibilityLabels

/**
 * The footer of the Hero Section, acting as both an expand/collapse toggle
 * and a navigation controller for the carousel pages.
 *
 * @param isExpanded Whether the Hero section is currently showing full details.
 * @param onToggleExpand Callback to open/close the budget details.
 * @param onPreviousPage Callback to swipe/navigate left.
 * @param onNextPage Callback to swipe/navigate right.
 * @param modifier The layout modifier.
 */
@Composable
fun HeroFooter(
    isExpanded: Boolean,
    onToggleExpand: () -> Unit,
    onPreviousPage: () -> Unit,
    onNextPage: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = AppTheme.dimens.small, horizontal = AppTheme.dimens.medium),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // --- PREVIOUS BUTTON (Left) ---
        Box(
            modifier = Modifier
                .weight(0.9f)
                .clip(CircleShape)
                .clickable(
                    role = Role.Button,
                    onClick = onPreviousPage
                )
                .padding(vertical = AppTheme.dimens.small),
            contentAlignment = Alignment.CenterStart
        ) {
            Icon(
                // Update this with your actual left arrow drawable
                painter = painterResource(id = R.drawable.caret_left),
                contentDescription = AccessibilityLabels.PREVIOUS_VIEW,
                tint = AppTheme.colors.textSecondary.toColor(),
                modifier = Modifier.padding(start = AppTheme.dimens.medium).size(20.dp)
            )
        }

        // --- EXPAND / COLLAPSE BUTTON (Center) ---
        val stateDesc = if (isExpanded) AccessibilityLabels.COLLAPSE_DESC else AccessibilityLabels.EXPAND_DESC
        val clickLabel = if (isExpanded) AccessibilityLabels.COLLAPSE_HERO else AccessibilityLabels.EXPAND_HERO

        Box(
            modifier = Modifier
                .weight(1f)
                .semantics { stateDescription = stateDesc }
                .clip(CircleShape)
                .clickable(
                    onClickLabel = clickLabel,
                    role = Role.Button,
                    onClick = onToggleExpand
                )
                .padding(vertical = AppTheme.dimens.small),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = if (isExpanded) R.drawable.caret_up else R.drawable.caret_down),
                contentDescription = null,
                tint = AppTheme.colors.textSecondary.toColor()
            )
        }

        // --- NEXT BUTTON (Right) ---
        Box(
            modifier = Modifier
                .weight(0.9f)
                .clip(CircleShape)
                .clickable(
                    role = Role.Button,
                    onClick = onNextPage
                )
                .padding(vertical = AppTheme.dimens.small),
            contentAlignment = Alignment.CenterEnd
        ) {
            Icon(
                painter = painterResource(id = R.drawable.caret_right),
                contentDescription = AccessibilityLabels.NEXT_VIEW,
                tint = AppTheme.colors.textSecondary.toColor(),
                modifier = Modifier.padding(end = AppTheme.dimens.medium).size(20.dp)
            )
        }
    }
}