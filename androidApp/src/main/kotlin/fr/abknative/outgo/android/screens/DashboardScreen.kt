package fr.abknative.outgo.android.screens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fr.abknative.outgo.android.components.*
import fr.abknative.outgo.android.ui.components.EmptyStateView
import fr.abknative.outgo.android.ui.components.LoaderItem
import fr.abknative.outgo.android.ui.states.ExpenseFilterType
import fr.abknative.outgo.android.ui.states.MainTab
import fr.abknative.outgo.android.ui.states.rememberOutgoingFormState
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

    var currentFilter by remember { mutableStateOf(ExpenseFilterType.ALL) }
    var currentTab by remember { mutableStateOf(MainTab.HOME) }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    val formState = rememberOutgoingFormState()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            Header(
                isConnected = state.isCloudSyncActive,
                onSyncIconClick = { showSyncModal = true }
            )
        },
        floatingActionButton = {
            AddActionTrigger(
                onClick = { showFormSheet = true }
            )
        },
        bottomBar = {
            BottomNavigationMenu(
                currentTab = currentTab,
                onTabSelected = { currentTab = it }
            )
        }
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {

            // --- En-tête : Le Budget (Hero Section) ---
            item {
                HeroSection(
                    monthlyBudgetInCents = state.monthlyIncomeInCents,
                    remainingToPayThisMonthInCents = state.remainingToPayInCents,
                    onEditBudgetClick = { showBudgetDialog = true }
                )
            }

            // --- Sélecteur de filtres ---
            item {
                ExpenseFilterSelector(
                    selectedFilter = currentFilter,
                    onFilterSelected = { currentFilter = it }
                )
            }

            // --- La Liste ---
            when {
                state.isLoading -> {
                    item { LoaderItem() }
                }
                state.outgoings.isEmpty() -> {
                    item { EmptyStateView() }
                }
                else -> {
                    val filteredList = state.outgoings // TODO: Appliquer le filtre 'currentFilter'

                    items(
                        items = filteredList,
                        key = { it.id }
                    ) { outgoing ->
                        OutgoingCard(
                            outgoing = outgoing,
                            onClick = {
                                // TODO: Mode Édition
                                // Pré-remplir un formState spécifique et ouvrir le Sheet
                            },
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }
    }

    if (showBudgetDialog) {
        BudgetEditDialog(
            initialBudgetInEuros = (state.monthlyIncomeInCents / 100.0).toString().replace(".0", ""),
            onDismiss = { showBudgetDialog = false },
            onSave = { newBudgetString ->
                val cents = (newBudgetString.toDoubleOrNull() ?: 0.0) * 100
                // Envoi de l'intention au domaine
                // presenter.onIntent(OutgoingIntent.UpdateBudget(cents.toLong()))
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
                onSave = {
                    // Création de l'objet ou envoi direct de l'Intent au Presenter
                    /* presenter.onIntent(
                        OutgoingIntent.Add(
                            name = formState.nameBuffer,
                            amountInCents = (formState.amountBuffer.toDouble() * 100).toLong(),
                            cycle = formState.cycleSelection,
                            billingDay = formState.billingDayBuffer.toInt()
                        )
                    ) */
                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                        if (!sheetState.isVisible) showFormSheet = false
                    }
                }
            )
        }
    }
}