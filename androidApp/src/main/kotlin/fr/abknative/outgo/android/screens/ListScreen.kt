package fr.abknative.outgo.android.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import fr.abknative.outgo.android.components.list.OperationFilterSelector
import fr.abknative.outgo.android.components.list.OperationListContainer
import fr.abknative.outgo.android.designsystem.components.feedback.AppSnackbar
import fr.abknative.outgo.android.designsystem.components.selection.MonthTimeSelector
import fr.abknative.outgo.android.designsystem.foundation.AppTheme
import fr.abknative.outgo.android.ui.extensions.getMonthName
import fr.abknative.outgo.android.ui.toUIString
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
                .padding(horizontal = AppTheme.dimens.medium)
        ) {
            MonthTimeSelector(
                formattedMonth = formattedSelectedMonth,
                canGoBack = state.canGoToPreviousMonth,
                onPrevious = { presenter.onIntent(ListIntent.NavigateMonth(isNext = false)) },
                onNext = { presenter.onIntent(ListIntent.NavigateMonth(isNext = true)) }
            )

            Spacer(modifier = Modifier.height(AppTheme.dimens.extraLarge))

            OperationFilterSelector(
                selectedFilter = state.currentFilter,
                onFilterSelected = { presenter.onIntent(ListIntent.UpdateFilter(it)) }
            )

            OperationListContainer(
                isLoading = state.isLoading,
                filteredList = state.filteredOperations,
                currentFilter = state.currentFilter,
                onDeleteRequest = { projectedOp -> operationToDelete = projectedOp },
                onEdit = { projectedOp ->
                    val op = projectedOp.operation
                    val formattedAmount = op.amountInCents.toBigDecimal()
                        .movePointLeft(2)
                        .toPlainString()

                    shellPresenter.onIntent(
                        ShellIntent.OpenOperationForm(
                            payload = OperationPayload(
                                id = op.id,
                                name = op.name,
                                amount = formattedAmount,
                                type = op.type,
                                recurrence = op.recurrence,
                                startDate = projectedOp.projectedDate,
                                endDate = projectedOp.projectedDate
                            )
                        )
                    )
                }
            )
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