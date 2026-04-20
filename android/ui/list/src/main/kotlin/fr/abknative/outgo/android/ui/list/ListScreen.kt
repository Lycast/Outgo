package fr.abknative.outgo.android.ui.list

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import fr.abknative.outgo.android.core.ListLabels
import fr.abknative.outgo.android.core.components.feedback.AppSnackbar
import fr.abknative.outgo.android.core.designsystem.AppTheme
import fr.abknative.outgo.android.core.extensions.getMonthName
import fr.abknative.outgo.android.core.toUIString
import fr.abknative.outgo.android.ui.list.components.ListFilterZone
import fr.abknative.outgo.android.ui.list.components.OperationListContainer
import fr.abknative.outgo.android.ui.list.components.ViewModeSelector
import fr.abknative.outgo.core.ui.DesignAnimations
import fr.abknative.outgo.list.api.ListIntent
import fr.abknative.outgo.list.api.ListPresenter
import fr.abknative.outgo.wallet.api.model.presenter.ProjectedOperation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(
    presenter: ListPresenter,
    onEditOperation: (ProjectedOperation) -> Unit,
    onDuplicateOperation: (ProjectedOperation) -> Unit,
    modifier: Modifier = Modifier
) {

    val state by presenter.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val currentError = state.error
    val errorMessage = currentError?.toUIString()
    val formattedSelectedMonth = "${getMonthName(state.selectedMonth)} ${state.selectedYear}".uppercase()

    LaunchedEffect(currentError) {
        if (currentError != null && errorMessage != null) {
            snackbarHostState.showSnackbar(message = errorMessage, withDismissAction = true)
            presenter.onIntent(ListIntent.DismissError)
        }
    }

    Box(
        modifier = modifier.fillMaxSize()
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            // 1. L'Interrupteur Principal (Fixe)
            ViewModeSelector(
                currentMode = state.viewMode,
                onModeChanged = { presenter.onIntent(ListIntent.SwitchViewMode(it)) },
                infoTitle = ListLabels.VIEW_MODE_TOOLTIP_TITLE,
                infoDescription = ListLabels.VIEW_MODE_TOOLTIP_DESC
            )

            Spacer(modifier = Modifier.height(AppTheme.dimens.small))

            // 2. La Zone Animée (Accordéon fluide)
            ListFilterZone(
                viewMode = state.viewMode,
                formattedMonth = formattedSelectedMonth,
                canGoBack = state.canGoToPreviousMonth,
                onPreviousMonth = { presenter.onIntent(ListIntent.NavigateMonth(isNext = false)) },
                onNextMonth = { presenter.onIntent(ListIntent.NavigateMonth(isNext = true)) },
                projectedFilter = state.projectedFilter,
                onProjectedFilterChange = { presenter.onIntent(ListIntent.UpdateProjectedFilter(it)) },
                standardFilter = state.standardFilter,
                onStandardFilterChange = { presenter.onIntent(ListIntent.UpdateStandardFilter(it)) }
            )

            AnimatedContent(
                targetState = state,
                transitionSpec = {
                    fadeIn(animationSpec = tween(DesignAnimations.NORMAL)) togetherWith
                            fadeOut(animationSpec = tween(DesignAnimations.NORMAL))
                },
                label = "ListCrossfade"
            ) { animatedState ->
                OperationListContainer(
                    isLoading = animatedState.isLoading,
                    viewMode = state.viewMode,
                    currentDateMillis = state.currentDateMillis,
                    groupedOperations = animatedState.groupedOperations,
                    onDeleteRequest = { projectedOp -> presenter.onIntent(ListIntent.Delete(projectedOp.operation.id)) },
                    onUnsubscribe = { projectedOp -> presenter.onIntent(ListIntent.EndSubscription(projectedOp)) },
                    onEdit = onEditOperation,
                    onDuplicate = onDuplicateOperation
                )
            }
        }
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.TopCenter)
        ) { data -> AppSnackbar(data) }
    }
}