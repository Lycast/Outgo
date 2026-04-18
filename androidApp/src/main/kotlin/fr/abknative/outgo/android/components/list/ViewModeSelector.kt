package fr.abknative.outgo.android.components.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import fr.abknative.outgo.android.R
import fr.abknative.outgo.android.designsystem.components.selection.AppSegmentedControl
import fr.abknative.outgo.android.designsystem.foundation.AppTheme
import fr.abknative.outgo.android.designsystem.foundation.toColor
import fr.abknative.outgo.android.ui.extensions.uiLabel
import fr.abknative.outgo.list.api.ListViewMode

@Composable
fun ViewModeSelector(
    currentMode: ListViewMode,
    onModeChanged: (ListViewMode) -> Unit,
    onInfoClicked: () -> Unit,
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

        // Le bouton info tout à droite
        IconButton(onClick = onInfoClicked) {
            Icon(
                painter = painterResource(id = R.drawable.info),
                contentDescription = "Aide sur les modes de vue",
                tint = AppTheme.colors.textPrimary.toColor()
            )
        }
    }
}