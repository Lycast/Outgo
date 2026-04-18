package fr.abknative.outgo.android.components.list

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fr.abknative.outgo.android.designsystem.components.selection.AppSegmentedControl
import fr.abknative.outgo.android.designsystem.components.selection.MonthTimeSelector
import fr.abknative.outgo.android.designsystem.foundation.AppTheme
import fr.abknative.outgo.android.ui.extensions.uiLabel
import fr.abknative.outgo.core.ui.DesignAnimations
import fr.abknative.outgo.list.api.ListViewMode
import fr.abknative.outgo.list.api.ProjectedFilter
import fr.abknative.outgo.list.api.StandardFilter

@Composable
fun ListFilterZone(
    viewMode: ListViewMode,
    // --- Params Projetés ---
    formattedMonth: String,
    canGoBack: Boolean,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit,
    projectedFilter: ProjectedFilter,
    onProjectedFilterChange: (ProjectedFilter) -> Unit,
    // --- Params Standards ---
    standardFilter: StandardFilter,
    onStandardFilterChange: (StandardFilter) -> Unit,
    modifier: Modifier = Modifier
) {

    val (filterLabels, selectedIndex) = when (viewMode) {
        ListViewMode.PROJECTED -> { val entries = ProjectedFilter.entries
            Pair(entries.map { it.uiLabel }, entries.indexOf(projectedFilter))
        }
        ListViewMode.STANDARD -> { val entries = StandardFilter.entries
            Pair(entries.map { it.uiLabel }, entries.indexOf(standardFilter))
        }
    }

    Column(modifier = modifier.fillMaxWidth().padding(horizontal = AppTheme.dimens.medium)) {

        AppSegmentedControl(
            items = filterLabels,
            selectedIndex = selectedIndex,
            onItemSelected = { newIndex ->
                when (viewMode) {
                    ListViewMode.PROJECTED -> {
                        val selectedEnum = ProjectedFilter.entries[newIndex]
                        onProjectedFilterChange(selectedEnum)
                    }
                    ListViewMode.STANDARD -> {
                        val selectedEnum = StandardFilter.entries[newIndex]
                        onStandardFilterChange(selectedEnum)
                    }
                }
            }
        )

        Spacer(modifier = Modifier.height(AppTheme.dimens.small))

        AnimatedContent(
            targetState = (viewMode == ListViewMode.PROJECTED),
            transitionSpec = {
                fadeIn(animationSpec = tween(durationMillis = DesignAnimations.NORMAL)) togetherWith
                        fadeOut(animationSpec = tween(durationMillis = DesignAnimations.NORMAL))
            },
            label = "MonthSelectorFade"
        ) { isProjectedView ->
            if (isProjectedView) {
                MonthTimeSelector(
                    formattedMonth = formattedMonth,
                    canGoBack = canGoBack,
                    textStyle = AppTheme.typo.body,
                    onPrevious = onPreviousMonth,
                    onNext = onNextMonth
                )
            } else {
                Spacer(modifier = Modifier.height(48.dp).fillMaxWidth())
            }
        }

        Spacer(modifier = Modifier.height(AppTheme.dimens.large))
    }
}