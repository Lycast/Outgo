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
import fr.abknative.outgo.android.components.dashboard.HeroSection
import fr.abknative.outgo.android.components.dashboard.OperationFilterSelector
import fr.abknative.outgo.android.components.dashboard.OperationListContainer
import fr.abknative.outgo.android.designsystem.components.buttons.AppFAB
import fr.abknative.outgo.android.designsystem.components.feedback.AppSnackbar
import fr.abknative.outgo.android.designsystem.foundation.AppTheme
import fr.abknative.outgo.android.ui.extensions.getMonthName
import fr.abknative.outgo.android.ui.states.rememberOperationFormState
import fr.abknative.outgo.android.ui.toUIString
import fr.abknative.outgo.core.api.TimeProvider
import fr.abknative.outgo.dashboard.api.DashboardIntent
import fr.abknative.outgo.dashboard.api.DashboardPresenter
import fr.abknative.outgo.wallet.api.model.dashboard.ProjectedOperation
import fr.abknative.outgo.wallet.api.model.operation.OperationType
import fr.abknative.outgo.wallet.api.model.operation.Recurrence
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    presenter: DashboardPresenter,
    isPremium: Boolean,
    onNavigateToLogin: () -> Unit,
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

    val formattedSelectedMonth = "${getMonthName(state.selectedMonth)} ${state.selectedYear}"

    LaunchedEffect(currentError) {
        if (currentError != null && errorMessage != null) {
            snackbarHostState.showSnackbar(message = errorMessage, withDismissAction = true)
            presenter.onIntent(DashboardIntent.DismissError)
        }
    }

    Box(
        modifier = modifier.fillMaxSize()
    ) {

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            HeroSection(
                isExpanded = state.isHeroExpanded,
                canGoToPreviousMonth = state.canGoToPreviousMonth,
                formattedMonthDate = formattedSelectedMonth,
                activeWalletName = state.activeWalletName,
                monthlyIncomeInCents = state.monthlyIncomeInCents,
                totalOutgoingsInCents = state.totalOutgoingsInCents,
                disposableIncomeInCents = state.disposableIncomeInCents,
                remainingToPayInCents = state.remainingToPayInCents,
                onToggleExpand = { presenter.onIntent(DashboardIntent.ToggleHeroSection(!state.isHeroExpanded)) },
                onPreviousMonthClick = { presenter.onIntent(DashboardIntent.NavigateMonth(isNext = false)) },
                onNextMonthClick = { presenter.onIntent(DashboardIntent.NavigateMonth(isNext = true)) },
                onEditBudgetClick = { showBudgetDialog = true }
            )

            Spacer(modifier = Modifier.height(AppTheme.dimens.extraLarge))

            OperationFilterSelector(
                selectedFilter = state.currentFilter,
                onFilterSelected = { presenter.onIntent(DashboardIntent.UpdateFilter(it)) }
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

        // Action Flottante
        if (state.activeWalletId != null) {
            AppFAB(
                onClick = { selectedOperation = null; showFormSheet = true },
                modifier = Modifier.align(Alignment.BottomEnd).padding(AppTheme.dimens.large)
            )
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.TopCenter)
        ) { data -> AppSnackbar(data) }
    }

    // --- MODALS ---
    DashboardModals(
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