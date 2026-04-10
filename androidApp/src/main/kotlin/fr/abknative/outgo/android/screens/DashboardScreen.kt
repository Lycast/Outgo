package fr.abknative.outgo.android.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import fr.abknative.outgo.android.components.common.ConfirmationDialog
import fr.abknative.outgo.android.components.common.PrimaryButton
import fr.abknative.outgo.android.components.common.SecondaryButton
import fr.abknative.outgo.android.components.dashboard.*
import fr.abknative.outgo.android.components.sheet.OperationFormSheet
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
    isPremium: Boolean,
    onNavigateToLogin: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by presenter.state.collectAsState()
    val timeProvider = koinInject<TimeProvider>()

    var operationToDelete by remember { mutableStateOf<ProjectedOperation?>(null) }
    var showBudgetDialog by remember { mutableStateOf(false) }
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
        initialAmount = selectedOperation?.operation?.amountInCents?.toBigDecimal()?.movePointLeft(2)?.toPlainString()
            ?: "",
        initialType = selectedOperation?.operation?.type ?: OperationType.EXPENSE,
        initialRecurrence = selectedOperation?.operation?.recurrence ?: Recurrence.MONTHLY,
    )

    val formattedSelectedMonth = "${getMonthName(state.selectedMonth)} ${state.selectedYear}"
    val currentDay = state.currentDay ?: 0
    val currentMonth = state.currentMonth
    val selectedMonth = state.selectedMonth

    val filteredList = remember(
        state.operations,
        currentFilter,
        currentDay,
        currentMonth,
        selectedMonth,
        state.selectedYear,
        currentYearNow,
        isPremium
    ) {
        val baseList = if (isPremium) {
            state.operations
        } else {
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

    val canGoToPreviousMonth =
        remember(state.selectedMonth, state.selectedYear, state.walletCreationMonth, state.walletCreationYear) {
            val currentAbsoluteMonth = state.selectedYear * 12 + state.selectedMonth
            val creationAbsoluteMonth =
                (state.walletCreationYear ?: state.selectedYear) * 12 + (state.walletCreationMonth
                    ?: state.selectedMonth)
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

    Box(
        modifier = modifier.fillMaxSize()
    ) {

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.TopCenter)
        )

        Column(
            modifier = Modifier.fillMaxSize()
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

        if (state.activeWalletId != null) {
            AddActionTrigger(
                onClick = { selectedOperation = null; showFormSheet = true },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(AppTheme.spacing.large)
            )
        }
        /* todo on enleve pour l'instant
        if (state.error is WalletError.NoActiveWallet) {
            Column(
                modifier = Modifier.fillMaxSize().padding(AppTheme.spacing.extraLarge),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Oups, espace introuvable 🕵️",
                    style = MaterialTheme.typography.headlineMedium,
                    color = AppTheme.colors.textPrimary.toColor()
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Il semble que vos données soient corrompues ou introuvables. Vous devez reconfigurer votre espace de départ.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = AppTheme.colors.textSecondary.toColor(),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
                Spacer(modifier = Modifier.height(32.dp))
                Button(
                    onClick = {
                        // Tu peux passer un callback `onCriticalError` depuis App.kt
                        // qui appellera coordinator.replaceRoot(AppStep.Onboarding)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Reconfigurer mon espace")
                }
            }

            // On "return" prématurément pour ne pas dessiner le reste du Dashboard cassé
            return
        }*/
    }

    // --- MODALS ---
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

    if (showFormSheet) {
        OperationFormSheet(
            formState = formState,
            sheetState = sheetState,
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
