package fr.abknative.outgo.android.components.dashboard

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
 * The footer of the Hero Section, containing the toggle to expand or collapse
 * the detailed budget views.
 *
 * @param isExpanded Whether the Hero section is currently showing full details.
 * @param onToggleExpand Callback to open/close the budget details.
 * @param modifier The layout modifier.
 */
@Composable
fun HeroFooter(
    isExpanded: Boolean,
    onToggleExpand: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = AppTheme.dimens.extraSmall)
            .padding(horizontal = AppTheme.dimens.big),
        contentAlignment = Alignment.Center
    ) {
        val stateDesc = if (isExpanded) AccessibilityLabels.COLLAPSE_DESC else AccessibilityLabels.EXPAND_DESC
        val clickLabel = if (isExpanded) AccessibilityLabels.COLLAPSE_HERO else AccessibilityLabels.EXPAND_HERO

        Box(
            modifier = Modifier
                .fillMaxWidth()
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
                tint = AppTheme.colors.textSecondary.toColor(),
                modifier = Modifier.size(24.dp)
            )
        }
    }
}