package fr.abknative.outgo.android.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import fr.abknative.outgo.android.components.list.ListFilterZone
import fr.abknative.outgo.android.components.list.OperationListContainer
import fr.abknative.outgo.android.components.list.ViewModeSelector
import fr.abknative.outgo.android.designsystem.components.feedback.AppSnackbar
import fr.abknative.outgo.android.designsystem.foundation.AppTheme
import fr.abknative.outgo.android.ui.ListLabels
import fr.abknative.outgo.android.ui.extensions.getMonthName
import fr.abknative.outgo.android.ui.toUIString
import fr.abknative.outgo.core.api.formatters.formatForInput
import fr.abknative.outgo.core.ui.DesignAnimations
import fr.abknative.outgo.list.api.ListIntent
import fr.abknative.outgo.list.api.ListPresenter
import fr.abknative.outgo.shell.api.ShellIntent
import fr.abknative.outgo.shell.api.ShellPresenter
import fr.abknative.outgo.shell.api.payload.OperationPayload
import fr.abknative.outgo.wallet.api.model.presenter.ProjectedOperation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(
    presenter: ListPresenter,
    shellPresenter: ShellPresenter,
    modifier: Modifier = Modifier
) {

    val state by presenter.state.collectAsStateWithLifecycle()
    var operationToDelete by remember { mutableStateOf<ProjectedOperation?>(null) }
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
                    fadeIn(animationSpec = tween(DesignAnimations.NORMAL)) togetherWith fadeOut(animationSpec = tween(DesignAnimations.NORMAL))
                },
                label = "ListCrossfade"
            ) { animatedState ->
                OperationListContainer(
                    isLoading = animatedState.isLoading,
                    viewMode = state.viewMode,
                    groupedOperations = animatedState.groupedOperations,
                    onDeleteRequest = { projectedOp -> operationToDelete = projectedOp },
                    onEdit = { projectedOp ->
                        val op = projectedOp.operation
                        val formattedAmount = op.amountInCents.formatForInput()

                        shellPresenter.onIntent(
                            ShellIntent.OpenOperationForm(
                                payload = OperationPayload(
                                    id = op.id,
                                    name = op.name,
                                    amount = formattedAmount,
                                    type = op.type,
                                    recurrence = op.recurrence,
                                    startDate = op.startDate,
                                    endDate = op.endDate
                                )
                            )
                        )
                    }
                )
            }
        }
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.TopCenter)
        ) { data -> AppSnackbar(data) }
    }

    // --- MODALS ---
    ListScreenModals(
        operationToDelete = operationToDelete,
        onDismissDelete = { operationToDelete = null },
        presenter = presenter,
    )
}