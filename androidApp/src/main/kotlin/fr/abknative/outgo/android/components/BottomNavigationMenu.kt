package fr.abknative.outgo.android.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.BarChart
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import fr.abknative.outgo.android.ui.NavigationLabels
import fr.abknative.outgo.android.ui.states.MainTab

@Composable
fun BottomNavigationMenu(
    currentTab: MainTab,
    onTabSelected: (MainTab) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier
    ) {
        NavigationBarItem(
            selected = currentTab == MainTab.HOME,
            onClick = { onTabSelected(MainTab.HOME) },
            icon = { Icon(Icons.Rounded.Home, contentDescription = null) },
            label = { Text(NavigationLabels.TAB_HOME) }
        )

        NavigationBarItem(
            selected = currentTab == MainTab.STATS,
            onClick = { onTabSelected(MainTab.STATS) },
            icon = { Icon(Icons.Rounded.BarChart, contentDescription = null) },
            label = { Text(NavigationLabels.TAB_STATS) }
        )

        NavigationBarItem(
            selected = currentTab == MainTab.SETTINGS,
            onClick = { onTabSelected(MainTab.SETTINGS) },
            icon = { Icon(Icons.Rounded.Settings, contentDescription = null) },
            label = { Text(NavigationLabels.TAB_SETTINGS) }
        )
    }
}