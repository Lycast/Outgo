package fr.abknative.outgo.android.screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import fr.abknative.outgo.android.components.common.*
import fr.abknative.outgo.android.components.dashboard.*
import fr.abknative.outgo.android.ui.CommonLabels
import fr.abknative.outgo.android.ui.DialogLabels
import fr.abknative.outgo.android.ui.extensions.getMonthName
import fr.abknative.outgo.android.ui.states.OperationFilter
import fr.abknative.outgo.android.ui.states.rememberOperationFormState
import fr.abknative.outgo.android.ui.theme.AppTheme
import fr.abknative.outgo.android.ui.theme.toColor
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
    onNavigateToSettings: () -> Unit,
    onNavigateToLogin: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by presenter.state.collectAsState()
    val timeProvider = koinInject<TimeProvider>()

    var operationToDelete by remember { mutableStateOf<ProjectedOperation?>(null) }
    var showBudgetDialog by remember { mutableStateOf(false) }
    var showSyncModal by remember { mutableStateOf(false) }
    var showFormSheet by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    var selectedOperation by remember { mutableStateOf<ProjectedOperation?>(null) }
    var currentFilter by remember { mutableStateOf(OperationFilter.ALL) }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val currentYearNow = timeProvider.yearValue(timeProvider.now())

    val formState = rememberOperationFormState(
        operationId = selectedOperation?.operation?.id,
        walletId = state.activeWalletId ?: "",
        timeProvider = timeProvider,
        initialName = selectedOperation?.operation?.name ?: "",
        initialAmount = selectedOperation?.operation?.amountInCents?.toBigDecimal()?.movePointLeft(2)?.toPlainString() ?: "",
        initialType = selectedOperation?.operation?.type ?: OperationType.EXPENSE,
        initialRecurrence = selectedOperation?.operation?.recurrence ?: Recurrence.MONTHLY,
        initialDay = selectedOperation?.operation?.startDate?.let { timeProvider.dayOfMonth(it).toString() } ?: timeProvider.dayOfMonth(timeProvider.now()).toString(),
        initialMonth = selectedOperation?.operation?.startDate?.let { timeProvider.monthValue(it).toString() } ?: state.selectedMonth.toString(),
        initialYear = selectedOperation?.operation?.startDate?.let { timeProvider.yearValue(it).toString() } ?: state.selectedYear.toString()
    )

    val formattedSelectedMonth = "${getMonthName(state.selectedMonth)} ${state.selectedYear}"
    val isPremium = state.isPremium
    val currentDay = state.currentDay ?: 0
    val currentMonth = state.currentMonth
    val selectedMonth = state.selectedMonth

    val filteredList = remember(state.operations, currentFilter, currentDay, currentMonth, selectedMonth, state.selectedYear, currentYearNow, isPremium) {
        val baseList = if (isPremium) { state.operations } else {
            state.operations.filter { it.operation.type == OperationType.EXPENSE }
        }
        val absoluteSelectedMonth = state.selectedYear * 12 + selectedMonth
        val absoluteCurrentMonth = currentYearNow * 12 + currentMonth

        when (currentFilter) {
            OperationFilter.ALL -> baseList
            OperationFilter.PAST -> {
                when {
                    absoluteSelectedMonth < absoluteCurrentMonth -> baseList
                    absoluteSelectedMonth > absoluteCurrentMonth -> emptyList()
                    else -> baseList.filter { timeProvider.dayOfMonth(it.projectedDate) < currentDay }
                }
            }
            OperationFilter.REMAINING -> {
                when {
                    absoluteSelectedMonth < absoluteCurrentMonth -> emptyList()
                    absoluteSelectedMonth > absoluteCurrentMonth -> baseList
                    else -> baseList.filter { timeProvider.dayOfMonth(it.projectedDate) >= currentDay }
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

    LaunchedEffect(state.isLoading, state.activeWalletId) {
        if (!state.isLoading && state.activeWalletId == null) {
            showBudgetDialog = true
        }
    }

    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    AppBackground {

        // --- COMPOSANT PRINCIPAL ---
        Scaffold(
            modifier = modifier.fillMaxSize(),
            containerColor = Color.Transparent,
            snackbarHost = { SnackbarHost(snackbarHostState) },
            topBar = {
                if (!isLandscape) {
                    Header(
                        syncState = state.syncState,
                        isVertical = false,
                        isSettingsScreen = false,
                        onSyncIconClick = {
                            when {
                                state.syncState.isUnauthenticated -> showSyncModal = true
                                state.syncState.isPending || state.syncState.isError || state.syncState.isUpToDate -> {
                                    presenter.onIntent(DashboardIntent.Refresh)
                                }
                            }
                        },
                        onSyncNavigationClick = { onNavigateToSettings() }
                    )
                }
            },
            floatingActionButton = {
                if (state.activeWalletId != null) {
                    AddActionTrigger(onClick = { selectedOperation = null; showFormSheet = true })
                }
            }
        ) { paddingValues ->

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(if (isLandscape) PaddingValues(0.dp) else paddingValues)
            ) {
                if (isLandscape) {
                    // Header Vertical à gauche
                    Header(
                        syncState = state.syncState,
                        isVertical = true,
                        isSettingsScreen = false,
                        onSyncIconClick = {
                            when {
                                state.syncState.isUnauthenticated -> showSyncModal = true
                                state.syncState.isPending || state.syncState.isError || state.syncState.isUpToDate -> {
                                    presenter.onIntent(DashboardIntent.Refresh)
                                }
                            }
                        },
                        onSyncNavigationClick = { onNavigateToSettings() }
                    )

                    VerticalDivider(
                        thickness = 1.dp,
                        color = AppTheme.colors.textSecondary.toColor().copy(alpha = 0.1f)
                    )
                }

                // Colonne principale avec les données
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                ) {
                    // Ajustement du padding supérieur si on est en paysage
                    val topSpacerHeight = if (isLandscape) AppTheme.spacing.medium else 0.dp
                    if (isLandscape) {
                        Spacer(modifier = Modifier.height(topSpacerHeight))
                    }

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
                                val (newMonth, newYear) = if (currentMonth == 1) {
                                    12 to (currentYear - 1)
                                } else {
                                    (currentMonth - 1) to currentYear
                                }
                                presenter.onIntent(DashboardIntent.SelectMonth(newMonth, newYear))
                            }
                        },
                        onNextMonthClick = {
                            val currentMonth = state.selectedMonth
                            val currentYear = state.selectedYear
                            val (newMonth, newYear) = if (currentMonth == 12) {
                                1 to (currentYear + 1)
                            } else {
                                (currentMonth + 1) to currentYear
                            }
                            presenter.onIntent(DashboardIntent.SelectMonth(newMonth, newYear))
                        },
                        onEditBudgetClick = { showBudgetDialog = true }
                    )

                    Spacer(modifier = Modifier.height(AppTheme.spacing.extraLarge))

                    OperationFilterSelector(
                        selectedFilter = currentFilter,
                        onFilterSelected = { currentFilter = it }
                    )

                    Spacer(modifier = Modifier.height(AppTheme.spacing.small))

                    // --- COMPOSANT LISTE ---
                    OperationListContainer(
                        isLoading = state.isLoading,
                        filteredList = filteredList,
                        currentFilter = currentFilter,
                        onDeleteRequest = { projectedOp -> operationToDelete = projectedOp },
                        onEdit = { outgoing ->
                            selectedOperation = outgoing
                            showFormSheet = true
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // --- MODALES ---
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

        if (showFormSheet) {
            OperationFormSheet(
                formState = formState,
                sheetState = sheetState,
                currentYear = currentYearNow,
                isPremium = isPremium,
                onEvent = { event -> formState.onEvent(event) },
                onDismiss = { showFormSheet = false },
                onSave = { intent -> presenter.onIntent(intent) },
                onDeleteRequest = {
                    showFormSheet = false
                    selectedOperation?.let { operationToDelete = it }
                },
                onDuplicateRequest = {
                    val originalName = selectedOperation?.operation?.name ?: ""
                    selectedOperation = selectedOperation?.copy(
                        operation = selectedOperation!!.operation.copy(
                            id = "",
                            name = "$originalName (Copie)"
                        )
                    )
                }
            )
        }

        if (operationToDelete != null) {
            ConfirmationDialog(
                title = DialogLabels.DELETE_OPERATION_TITLE,
                description = DialogLabels.DELETE_OPERATION_DESC,
                onDismiss = { operationToDelete = null },

                confirmButton = {
                    PrimaryButton(
                        label = CommonLabels.ACTION_DELETE,
                        labelColor = AppTheme.colors.error.toColor(),
                        containerColor = AppTheme.colors.primary.toColor().copy(alpha = 0.1f),
                        onClick = {
                            operationToDelete?.let { presenter.onIntent(DashboardIntent.Delete(it.operation.id)) }
                            operationToDelete = null
                        }
                    )
                },

                dismissButton = {
                    SecondaryButton(
                        label = CommonLabels.ACTION_CANCEL,
                        labelColor = AppTheme.colors.textSecondary.toColor(),
                        onClick = { operationToDelete = null },
                        modifier = Modifier.padding(end = AppTheme.spacing.medium)
                    )
                }
            )
        }
    }
}