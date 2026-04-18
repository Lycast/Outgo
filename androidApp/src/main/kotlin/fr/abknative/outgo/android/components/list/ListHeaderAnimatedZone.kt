package fr.abknative.outgo.android.components.list

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import fr.abknative.outgo.android.designsystem.components.selection.MonthTimeSelector
import fr.abknative.outgo.android.designsystem.foundation.AppTheme
import fr.abknative.outgo.android.ui.extensions.uiLabel
import fr.abknative.outgo.list.api.ListViewMode
import fr.abknative.outgo.list.api.ProjectedFilter
import fr.abknative.outgo.list.api.StandardFilter

@Composable
fun ListHeaderAnimatedZone(
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
    // AnimatedContent va faire un fondu croisé automatique dès que viewMode change
    AnimatedContent(
        targetState = viewMode,
        transitionSpec = {
            fadeIn(animationSpec = tween(300)) togetherWith fadeOut(animationSpec = tween(300))
        },
        label = "HeaderAnimation",
        modifier = modifier.fillMaxWidth()
    ) { targetMode ->

        Column(modifier = Modifier.fillMaxWidth()) {
            when (targetMode) {
                ListViewMode.PROJECTED -> {
                    // En mode Enveloppe, on affiche le Mois PUIS les filtres Projetés
                    MonthTimeSelector(
                        formattedMonth = formattedMonth,
                        canGoBack = canGoBack,
                        onPrevious = onPreviousMonth,
                        onNext = onNextMonth
                    )

                    Spacer(modifier = Modifier.height(AppTheme.dimens.medium))

                    FilterSelector(
                        items = ProjectedFilter.entries,
                        selectedItem = projectedFilter,
                        onItemSelected = onProjectedFilterChange,
                        labelMapper = { it.uiLabel }
                    )
                }

                ListViewMode.STANDARD -> {
                    // En mode Moteur, PAS de mois, juste la longue liste des filtres Standards
                    FilterSelector(
                        items = StandardFilter.entries,
                        selectedItem = standardFilter,
                        onItemSelected = onStandardFilterChange,
                        labelMapper = { it.uiLabel }
                    )
                }
            }
        }
    }
}