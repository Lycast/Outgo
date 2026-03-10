package fr.abknative.outgo.android.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import fr.abknative.outgo.android.components.*
import fr.abknative.outgo.android.ui.getMonthName
import fr.abknative.outgo.android.ui.states.OutgoingFilter
import fr.abknative.outgo.android.ui.states.rememberOutgoingFormState
import fr.abknative.outgo.android.ui.theme.AppTheme
import fr.abknative.outgo.android.ui.toUIString
import fr.abknative.outgo.outgoing.api.Recurrence
import fr.abknative.outgo.outgoing.api.model.Outgoing
import fr.abknative.outgo.outgoing.api.presenter.OutgoingIntent
import fr.abknative.outgo.outgoing.api.presenter.OutgoingPresenter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    presenter: OutgoingPresenter,
    onNavigateToSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by presenter.state.collectAsState()

    var showBudgetDialog by remember { mutableStateOf(false) }
    var showSyncModal by remember { mutableStateOf(false) }
    var showFormSheet by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    var selectedOutgoing by remember { mutableStateOf<Outgoing?>(null) }
    var currentFilter by remember { mutableStateOf(OutgoingFilter.ALL) }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val formState = rememberOutgoingFormState(
        outgoingId = selectedOutgoing?.id,
        initialName = selectedOutgoing?.name ?: "",
        initialAmount = selectedOutgoing?.amountInCents?.let { (it / 100.0).toString() } ?: "",
        initialRecurrence = selectedOutgoing?.recurrence ?: Recurrence.MONTHLY,
        initialDueDay = selectedOutgoing?.dueDay?.toString() ?: "",
        initialDueMonth = selectedOutgoing?.dueMonth?.toString() ?: ""
    )

    val formattedMonth = remember(state.currentMonth) { getMonthName(state.currentMonth) }
    val currentDay = state.currentDay ?: 0
    val filteredList = remember(state.outgoings, currentFilter, currentDay) {
        when (currentFilter) {
            OutgoingFilter.ALL -> state.outgoings
            OutgoingFilter.PAID -> state.outgoings.filter { it.dueDay < currentDay }
            OutgoingFilter.REMAINING -> state.outgoings.filter { it.dueDay >= currentDay }
        }
    }

    val currentError = state.error
    val errorMessage = currentError?.toUIString()

    LaunchedEffect(currentError) {
        if (currentError != null && errorMessage != null) {
            snackbarHostState.showSnackbar(message = errorMessage, withDismissAction = true)
            presenter.onIntent(OutgoingIntent.DismissError)
        }
    }

    LaunchedEffect(state.isLoading, state.monthlyIncomeInCents) {
        if (!state.isLoading && state.monthlyIncomeInCents <= 0L) {
            showBudgetDialog = true
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            Header(
                isConnected = state.isCloudSyncActive,
                isSettingsScreen = false,
                onSyncIconClick = { showSyncModal = true },
                onSyncNavigationClick = { onNavigateToSettings() }
            )
        },
        floatingActionButton = {
            AddActionTrigger(
                onClick = {
                    selectedOutgoing = null
                    showFormSheet = true
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            HeroSection(
                formattedTodayDate = formattedMonth,
                monthlyIncomeInCents = state.monthlyIncomeInCents,
                totalOutgoingsInCents = state.totalOutgoingsInCents,
                disposableIncomeInCents = state.disposableIncomeInCents,
                remainingToPayInCents = state.remainingToPayInCents,
                onEditIncomeClick = { showBudgetDialog = true }
            )

            Spacer(modifier = Modifier.height(AppTheme.spacing.large))

            ExpenseFilterSelector(
                selectedFilter = currentFilter,
                onFilterSelected = { currentFilter = it }
            )

            Spacer(modifier = Modifier.height(AppTheme.spacing.extraSmall))

            // --- APPEL DU COMPOSANT LISTE ---
            ExpenseListContainer(
                isLoading = state.isLoading,
                filteredList = filteredList,
                currentFilter = currentFilter,
                onDelete = { id -> presenter.onIntent(OutgoingIntent.Delete(id)) },
                onEdit = { outgoing ->
                    selectedOutgoing = outgoing
                    showFormSheet = true
                },
                modifier = Modifier.weight(1f)
            )
        }
    }

    if (showBudgetDialog) {
        BudgetEditDialog(
            currentIncomeInCents = state.monthlyIncomeInCents,
            onDismiss = { showBudgetDialog = false },
            onConfirm = { newIncomeInCents ->
                presenter.onIntent(OutgoingIntent.UpdateIncome(newIncomeInCents))
                showBudgetDialog = false
            }
        )
    }

    if (showSyncModal) {
        SyncPromotionModal(
            onDismiss = { showSyncModal = false },
            onNavigateToLogin = { showSyncModal = false }
        )
    }

    // --- APPEL DU COMPOSANT MODALE ---
    if (showFormSheet) {
        OutgoingFormSheet(
            formState = formState,
            sheetState = sheetState,
            onEvent = { event -> formState.onEvent(event) },
            onDismiss = { showFormSheet = false },
            onSave = { intent -> presenter.onIntent(intent) },
            onDuplicate = if (formState.outgoingId != null) { intent -> presenter.onIntent(intent) } else null,
            onDelete = if (formState.outgoingId != null) { id -> presenter.onIntent(OutgoingIntent.Delete(id)) } else null
        )
    }
}