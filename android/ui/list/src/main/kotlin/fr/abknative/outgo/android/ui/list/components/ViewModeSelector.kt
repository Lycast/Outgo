package fr.abknative.outgo.android.ui.list.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import fr.abknative.outgo.android.core.AccessibilityLabels
import fr.abknative.outgo.android.core.R
import fr.abknative.outgo.android.core.components.feedback.InfoTooltip
import fr.abknative.outgo.android.core.components.selection.AppSegmentedControl
import fr.abknative.outgo.android.core.designsystem.AppTheme
import fr.abknative.outgo.android.core.designsystem.toColor
import fr.abknative.outgo.android.ui.list.uiLabel
import fr.abknative.outgo.list.api.ListViewMode

@Composable
fun ViewModeSelector(
    currentMode: ListViewMode,
    onModeChanged: (ListViewMode) -> Unit,
    infoTitle: String,
    infoDescription: String,
    modifier: Modifier = Modifier
) {
    val modes = ListViewMode.entries.toTypedArray()
    val labels = modes.map { it.uiLabel }
    val selectedIndex = modes.indexOf(currentMode)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = AppTheme.dimens.medium),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(AppTheme.dimens.small)
    ) {

        AppSegmentedControl(
            items = labels,
            selectedIndex = selectedIndex,
            onItemSelected = { index -> onModeChanged(modes[index]) },
            modifier = Modifier.weight(1f)
        )

        InfoTooltip(
            title = infoTitle,
            description = infoDescription
        ) {
            Icon(
                painter = painterResource(id = R.drawable.info),
                contentDescription = AccessibilityLabels.VIEW_MODE,
                tint = AppTheme.colors.textPrimary.toColor()
            )
        }
    }
}