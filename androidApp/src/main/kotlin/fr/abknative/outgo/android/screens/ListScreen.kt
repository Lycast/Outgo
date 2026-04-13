package fr.abknative.outgo.android.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import fr.abknative.outgo.android.components.list.ListScreenModals
import fr.abknative.outgo.android.components.list.OperationListContainer
import fr.abknative.outgo.android.components.operation.OperationFilterSelector
import fr.abknative.outgo.android.designsystem.components.feedback.AppSnackbar
import fr.abknative.outgo.android.designsystem.components.selection.MonthTimeSelector
import fr.abknative.outgo.android.designsystem.foundation.AppTheme
import fr.abknative.outgo.android.ui.extensions.getMonthName
import fr.abknative.outgo.android.ui.states.rememberOperationFormState
import fr.abknative.outgo.android.ui.toUIString
import fr.abknative.outgo.core.api.TimeProvider
import fr.abknative.outgo.list.api.ListIntent
import fr.abknative.outgo.list.api.ListPresenter
import fr.abknative.outgo.shell.api.ShellState
import fr.abknative.outgo.wallet.api.model.operation.OperationType
import fr.abknative.outgo.wallet.api.model.operation.Recurrence
import fr.abknative.outgo.wallet.api.model.presenter.ProjectedOperation
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(
    presenter: ListPresenter,
    shellState: ShellState,
    onConsumeTrigger: () -> Unit,
    isPremium: Boolean,
    modifier: Modifier = Modifier
) {
    val state by presenter.state.collectAsStateWithLifecycle()
    val timeProvider = koinInject<TimeProvider>()

    // Local UI states strictly for Dialogs & Sheets
    var operationToDelete by remember { mutableStateOf<ProjectedOperation?>(null) }
    var showBudgetDialog by remember { mutableStateOf(false) }
    var showFormSheet by remember { mutableStateOf(false) }
    var selectedOperation by remember { mutableStateOf<ProjectedOperation?>(null) }

    val snackbarHostState = remember { SnackbarHostState() }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val formState = rememberOperationFormState(
        operationId = selectedOperation?.operation?.id,
        walletId = state.activeWalletId ?: "",
        timeProvider = timeProvider,
        initialName = selectedOperation?.operation?.name ?: "",
        initialAmount = selectedOperation?.operation?.amountInCents?.toBigDecimal()?.movePointLeft(2)?.toPlainString() ?: "",
        initialType = selectedOperation?.operation?.type ?: OperationType.EXPENSE,
        initialRecurrence = selectedOperation?.operation?.recurrence ?: Recurrence.MONTHLY,
    )

    val currentError = state.error
    val errorMessage = currentError?.toUIString()

    val formattedSelectedMonth = "${getMonthName(state.selectedMonth)} ${state.selectedYear}".uppercase()

    LaunchedEffect(currentError) {
        if (currentError != null && errorMessage != null) {
            snackbarHostState.showSnackbar(message = errorMessage, withDismissAction = true)
            presenter.onIntent(ListIntent.DismissError)
        }
    }

    LaunchedEffect(shellState.showAddOperationTrigger) {
        if (shellState.showAddOperationTrigger) {
            selectedOperation = null
            showFormSheet = true
            onConsumeTrigger()
        }
    }

    Box(
        modifier = modifier.fillMaxSize()
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = AppTheme.dimens.medium)
                .padding(horizontal = AppTheme.dimens.large)
        ) {
            // 1. Le Sélecteur de temps
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

            Spacer(modifier = Modifier.height(AppTheme.dimens.small))

            OperationListContainer(
                isLoading = state.isLoading,
                filteredList = state.filteredOperations,
                currentFilter = state.currentFilter,
                onDeleteRequest = { projectedOp -> operationToDelete = projectedOp },
                onEdit = { outgoing ->
                    selectedOperation = outgoing
                    showFormSheet = true
                },
                modifier = Modifier.weight(1f)
            )
        }
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.TopCenter)
        ) { data -> AppSnackbar(data) }
    }

    // --- MODALS ---
    ListScreenModals(
        state = state,
        showBudgetDialog = showBudgetDialog,
        showFormSheet = showFormSheet,
        operationToDelete = operationToDelete,
        selectedOperation = selectedOperation,
        formState = formState,
        sheetState = sheetState,
        onDismissBudget = { showBudgetDialog = false },
        onDismissForm = { showFormSheet = false },
        onDismissDelete = { operationToDelete = null },
        onOperationToDeleteChange = { operationToDelete = it },
        onSelectedOperationChange = { selectedOperation = it },
        presenter = presenter,
        timeProvider = timeProvider,
        isPremium = isPremium
    )
}