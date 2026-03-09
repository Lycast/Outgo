package fr.abknative.outgo.android.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import fr.abknative.outgo.android.components.*
import fr.abknative.outgo.android.ui.CommonLabels
import fr.abknative.outgo.android.ui.components.EmptyStateView
import fr.abknative.outgo.android.ui.components.LoaderItem
import fr.abknative.outgo.android.ui.states.MainTab
import fr.abknative.outgo.android.ui.states.OutgoingFilter
import fr.abknative.outgo.android.ui.states.rememberOutgoingFormState
import fr.abknative.outgo.android.ui.toUIString
import fr.abknative.outgo.android.ui.uiTodayDate
import fr.abknative.outgo.outgoing.api.Recurrence
import fr.abknative.outgo.outgoing.api.model.Outgoing
import fr.abknative.outgo.outgoing.api.presenter.OutgoingIntent
import fr.abknative.outgo.outgoing.api.presenter.OutgoingPresenter
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    presenter: OutgoingPresenter,
    modifier: Modifier = Modifier
) {

    val state by presenter.state.collectAsState()

    var showBudgetDialog by remember { mutableStateOf(false) }
    var showSyncModal by remember { mutableStateOf(false) }
    var showFormSheet by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    var selectedOutgoing by remember { mutableStateOf<Outgoing?>(null) }

    var currentFilter by remember { mutableStateOf(OutgoingFilter.ALL) }
    var currentTab by remember { mutableStateOf(MainTab.HOME) }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    val formState = rememberOutgoingFormState(
        outgoingId = selectedOutgoing?.id,
        initialName = selectedOutgoing?.name ?: "",
        initialAmount = selectedOutgoing?.amountInCents?.let { (it / 100.0).toString() } ?: "",
        initialRecurrence = selectedOutgoing?.recurrence ?: Recurrence.MONTHLY,
        initialDueDay = selectedOutgoing?.dueDay?.toString() ?: "",
        initialDueMonth = selectedOutgoing?.dueMonth?.toString() ?: ""
    )

    val formattedDate = remember(state.todayLabel) { state.todayLabel.uiTodayDate }
    val currentDay = remember(state.todayLabel) { state.todayLabel.split("|").firstOrNull()?.toIntOrNull() ?: 1 }
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
            snackbarHostState.showSnackbar(
                message = errorMessage,
                withDismissAction = true
            )
            presenter.onIntent(OutgoingIntent.DismissError)
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            Header(
                isConnected = state.isCloudSyncActive,
                onSyncIconClick = { showSyncModal = true }
            )
        },
        floatingActionButton = {
            AddActionTrigger(
                onClick = {
                    selectedOutgoing = null // Mode Création
                    showFormSheet = true
                }
            )
        },
        bottomBar = {
            BottomNavigationMenu(
                currentTab = currentTab,
                onTabSelected = { currentTab = it }
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            HeroSection(
                formattedTodayDate = formattedDate,
                monthlyIncomeInCents = state.monthlyIncomeInCents,
                totalOutgoingsInCents = state.totalOutgoingsInCents,
                disposableIncomeInCents = state.disposableIncomeInCents,
                remainingToPayInCents = state.remainingToPayInCents,
                onEditIncomeClick = { showBudgetDialog = true }
            )

            ExpenseFilterSelector(
                selectedFilter = currentFilter,
                onFilterSelected = { currentFilter = it }
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {

                // --- La Liste ---
                when {
                    state.isLoading -> {
                        item { LoaderItem() }
                    }

                    state.outgoings.isEmpty() -> {
                        item(key = "empty_state") { EmptyStateView() }
                    }

                    else -> {

                        items(
                            items = filteredList,
                            key = { it.id }
                        ) { outgoing ->

                            val dismissState = rememberSwipeToDismissBoxState(
                                confirmValueChange = { dismissValue ->

                                    if (dismissValue == SwipeToDismissBoxValue.EndToStart) {
                                        presenter.onIntent(OutgoingIntent.Delete(outgoing.id))
                                        true
                                    } else {
                                        false
                                    }
                                },
                                // Il faut glisser sur au moins 40% de l'écran pour déclencher la suppression.
                                positionalThreshold = { totalDistance -> totalDistance * 0.4f }
                            )

                            SwipeToDismissBox(
                                state = dismissState,
                                enableDismissFromStartToEnd = false,
                                backgroundContent = {
                                    val isThresholdCrossed = dismissState.targetValue == SwipeToDismissBoxValue.EndToStart

                                    val color by animateColorAsState(
                                        targetValue = if (isThresholdCrossed) {
                                            MaterialTheme.colorScheme.errorContainer
                                        } else {
                                            MaterialTheme.colorScheme.surfaceVariant
                                        },
                                        label = "swipe_color_animation"
                                    )

                                    val scale by animateFloatAsState(
                                        targetValue = if (isThresholdCrossed) 1.2f else 1.0f,
                                        label = "swipe_scale_animation"
                                    )

                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(horizontal = 16.dp, vertical = 4.dp)
                                            .background(color, shape = MaterialTheme.shapes.medium)
                                            .padding(horizontal = 20.dp),
                                        contentAlignment = Alignment.CenterEnd
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = CommonLabels.ACTION_DELETE,
                                            tint = if (isThresholdCrossed) MaterialTheme.colorScheme.onErrorContainer
                                            else MaterialTheme.colorScheme.onSurfaceVariant,
                                            modifier = Modifier.scale(scale)
                                        )
                                    }
                                },
                                // La carte elle-même
                                content = {
                                    OutgoingCard(
                                        outgoing = outgoing,
                                        onClick = {
                                            selectedOutgoing = outgoing // Mode Édition
                                            showFormSheet = true
                                        },
                                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                                    )
                                }
                            )
                        }
                    }
                }
            }
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
            onNavigateToLogin = {
                showSyncModal = false
                // TODO: Naviguer vers l'écran d'authentification (Semaine 5)
            }
        )
    }

    if (showFormSheet) {
        ModalBottomSheet(
            onDismissRequest = { showFormSheet = false },
            sheetState = sheetState
        ) {
            OutgoingFormContent(
                state = formState,
                onEvent = { event -> formState.onEvent(event) },
                onCancel = {
                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                        if (!sheetState.isVisible) showFormSheet = false
                    }
                },
                // --- SAUVEGARDE (Création ou Mise à jour) ---
                onSave = {
                    presenter.onIntent(
                        OutgoingIntent.Save(
                            id = formState.outgoingId,
                            name = formState.nameBuffer,
                            amountInCents = formState.amountInCents,
                            recurrence = formState.recurrenceSelection,
                            dueDay = formState.dueDayBuffer.toIntOrNull() ?: 1,
                            dueMonth = formState.dueMonthBuffer.toIntOrNull()
                        )
                    )
                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                        if (!sheetState.isVisible) showFormSheet = false
                    }
                },
                // --- SUPPRESSION DEPUIS LE FORMULAIRE ---
                onDelete = if (formState.outgoingId != null) {
                    {
                        presenter.onIntent(OutgoingIntent.Delete(formState.outgoingId!!))
                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                            if (!sheetState.isVisible) showFormSheet = false
                        }
                    }
                } else null
            )
        }
    }
}