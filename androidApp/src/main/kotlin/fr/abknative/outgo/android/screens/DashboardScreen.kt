package fr.abknative.outgo.android.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import fr.abknative.outgo.android.components.common.Header
import fr.abknative.outgo.android.components.common.SyncPromotionModal
import fr.abknative.outgo.android.components.dashboard.*
import fr.abknative.outgo.android.ui.extensions.getMonthName
import fr.abknative.outgo.android.ui.states.OperationFilter
import fr.abknative.outgo.android.ui.states.rememberOperationFormState
import fr.abknative.outgo.android.ui.theme.AppTheme
import fr.abknative.outgo.android.ui.toUIString
import fr.abknative.outgo.core.api.TimeProvider
import fr.abknative.outgo.dashboard.api.DashboardIntent
import fr.abknative.outgo.dashboard.api.DashboardPresenter
import fr.abknative.outgo.wallet.api.model.Operation
import fr.abknative.outgo.wallet.api.model.operation.OperationType
import fr.abknative.outgo.wallet.api.model.operation.Recurrence
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    presenter: DashboardPresenter,
    onNavigateToSettings: () -> Unit,
    onNavigateToLogin: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by presenter.state.collectAsState()
    val timeProvider = koinInject<TimeProvider>()

    var showBudgetDialog by remember { mutableStateOf(false) }
    var showSyncModal by remember { mutableStateOf(false) }
    var showFormSheet by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    var selectedOperation by remember { mutableStateOf<Operation?>(null) }
    var currentFilter by remember { mutableStateOf(OperationFilter.ALL) }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val formState = rememberOperationFormState(
        operationId = selectedOperation?.id,
        walletId = state.activeWalletId ?: "",
        timeProvider = timeProvider,
        initialName = selectedOperation?.name ?: "",
        initialAmount = selectedOperation?.amountInCents?.toBigDecimal()?.movePointLeft(2)?.toPlainString() ?: "",
        initialType = selectedOperation?.type ?: OperationType.EXPENSE,
        initialRecurrence = selectedOperation?.recurrence ?: Recurrence.MONTHLY,
        initialDay = selectedOperation?.startDate?.let { timeProvider.dayOfMonth(it).toString() } ?: ""
    )

    val formattedSelectedMonth = "${getMonthName(state.selectedMonth)} ${state.selectedYear}"

    // TODO: À l'avenir, ce booléen viendra de ton DashboardState (ex: state.isPremium)
    // Pour l'instant, on le force à false pour finaliser l'expérience Gratuite.
    val isPremium = false

    val currentDay = state.currentDay ?: 0
    val currentMonth = state.currentMonth
    val selectedMonth = state.selectedMonth
    val filteredList = remember(state.operations, currentFilter, currentDay, currentMonth, selectedMonth) {
        val baseList = if (isPremium) { state.operations } else { state.operations.filter { it.type == OperationType.EXPENSE } }
        when (currentFilter) {
            OperationFilter.ALL -> baseList
            OperationFilter.PAID -> {
                when {
                    selectedMonth < currentMonth -> baseList
                    selectedMonth > currentMonth -> emptyList()
                    else -> baseList.filter { timeProvider.dayOfMonth(it.startDate) < currentDay }
                }
            }

            OperationFilter.REMAINING -> {
                when {
                    selectedMonth < currentMonth -> emptyList()
                    selectedMonth > currentMonth -> baseList
                    else -> baseList.filter { timeProvider.dayOfMonth(it.startDate) >= currentDay }
                }
            }
        }
    }

    val canGoToPreviousMonth = remember(state.selectedMonth, state.selectedYear, state.walletCreationMonth, state.walletCreationYear) {
        val currentAbsoluteMonth = state.selectedYear * 12 + state.selectedMonth
        val creationAbsoluteMonth = (state.walletCreationYear ?: state.selectedYear) * 12 + (state.walletCreationMonth ?: state.selectedMonth)
        currentAbsoluteMonth > creationAbsoluteMonth
    }

    val currentError = state.error
    val errorMessage = currentError?.toUIString()

    LaunchedEffect(currentError) {
        if (currentError != null && errorMessage != null) {
            snackbarHostState.showSnackbar(message = errorMessage, withDismissAction = true)
            presenter.onIntent(DashboardIntent.DismissError)
        }
    }

    LaunchedEffect(state.isLoading, state.monthlyIncomeInCents) {
        if (!state.isLoading && state.monthlyIncomeInCents <= 0L) {
            showBudgetDialog = true
        }
    }

    // --- COMPOSANT PRINCIPAL ---
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = Color.Transparent,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            Header(
                syncState = state.syncState,
                isSettingsScreen = false,
                onSyncIconClick = {
                    if (state.syncState.isUnauthenticated) {
                        showSyncModal = true
                    } else if (state.syncState.isUpToDate || state.syncState.isError) {
                        presenter.onIntent(DashboardIntent.Refresh)
                    }
                },
                onSyncNavigationClick = { onNavigateToSettings() }
            )
        },
        floatingActionButton = {
            if (state.activeWalletId != null) {
                AddActionTrigger(onClick = { selectedOperation = null; showFormSheet = true })
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            HeroSection(
                isExpanded = state.isHeroExpanded,
                canGoToPreviousMonth = canGoToPreviousMonth,
                onToggleExpand = { presenter.onIntent(DashboardIntent.ToggleHeroSection(!state.isHeroExpanded)) },
                formattedMonthDate = formattedSelectedMonth,
                activeWalletName = state.activeWalletName,
                monthlyIncomeInCents = state.monthlyIncomeInCents,
                totalOutgoingsInCents = state.totalOutgoingsInCents,
                disposableIncomeInCents = state.disposableIncomeInCents,
                remainingToPayInCents = state.remainingToPayInCents,
                onPreviousMonthClick = {
                    if (canGoToPreviousMonth) {
                        val currentMonth = state.selectedMonth
                        val currentYear = state.selectedYear
                        val (newMonth, newYear) = if (currentMonth == 1) { 12 to (currentYear - 1)
                        } else { (currentMonth - 1) to currentYear }
                        presenter.onIntent(DashboardIntent.SelectMonth(newMonth, newYear))
                    }
                },
                onNextMonthClick = {
                    val currentMonth = state.selectedMonth
                    val currentYear = state.selectedYear
                    val (newMonth, newYear) = if (currentMonth == 12) { 1 to (currentYear + 1)
                    } else { (currentMonth + 1) to currentYear }
                    presenter.onIntent(DashboardIntent.SelectMonth(newMonth, newYear))
                },
                onEditBudgetClick = { showBudgetDialog = true }
            )

            Spacer(modifier = Modifier.height(AppTheme.spacing.extraLarge))

            ExpenseFilterSelector(
                selectedFilter = currentFilter,
                onFilterSelected = { currentFilter = it }
            )

            Spacer(modifier = Modifier.height(AppTheme.spacing.small))

            // --- COMPOSANT LISTE ---
            ExpenseListContainer(
                isLoading = state.isLoading,
                filteredList = filteredList,
                currentFilter = currentFilter,
                onDelete = { id -> presenter.onIntent(DashboardIntent.Delete(id)) },
                onEdit = { outgoing ->
                    selectedOperation = outgoing
                    showFormSheet = true
                },
                modifier = Modifier.weight(1f)
            )
        }
    }

    if (showBudgetDialog) {
        WalletEditDialog(
            initialWalletName = state.activeWalletName,
            currentIncomeInCents = state.monthlyIncomeInCents,
            onDismiss = { showBudgetDialog = false },
            onConfirm = { newName, newIncomeInCents ->
                val currentYear = timeProvider.yearValue(timeProvider.now())
                val startOfSelectedMonth = timeProvider.startOfMonth(state.selectedMonth, currentYear)

                presenter.onIntent(
                    DashboardIntent.SaveWalletAndIncome(
                        walletId = state.activeWalletId ?: "",
                        walletName = newName,
                        incomeAmountInCents = newIncomeInCents,
                        startDate = startOfSelectedMonth
                    )
                )
                showBudgetDialog = false
            }
        )
    }

    if (showSyncModal) {
        SyncPromotionModal(
            onDismiss = { showSyncModal = false },
            onNavigateToLogin = {
                showSyncModal = false
                onNavigateToLogin()
            }
        )
    }

    // --- COMPOSANT MODALE ---
    if (showFormSheet) {
        OperationFormSheet(
            formState = formState,
            sheetState = sheetState,
            onEvent = { event -> formState.onEvent(event) },
            onDismiss = { showFormSheet = false },
            onSave = { intent -> presenter.onIntent(intent) }
        )
    }
}