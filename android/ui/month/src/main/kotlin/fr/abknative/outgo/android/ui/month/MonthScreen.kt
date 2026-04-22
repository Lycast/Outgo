package fr.abknative.outgo.android.ui.month

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import fr.abknative.outgo.android.core.components.selection.MonthTimeSelector
import fr.abknative.outgo.android.core.designsystem.AppTheme
import fr.abknative.outgo.android.core.designsystem.toColor
import fr.abknative.outgo.android.core.extensions.getMonthName
import fr.abknative.outgo.android.core.toCommonUIString
import fr.abknative.outgo.android.ui.month.components.*
import fr.abknative.outgo.month.api.MonthIntent
import fr.abknative.outgo.month.api.MonthPresenter
import java.util.Locale.getDefault

@Composable
fun MonthScreen(
    presenter: MonthPresenter,
    onError: (String) -> Unit,
    onNavigateToList: () -> Unit,
    onAddOperationClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    val state by presenter.state.collectAsStateWithLifecycle()
    val formattedSelectedMonth = "${getMonthName(state.selectedMonth)} ${state.selectedYear}".uppercase(getDefault())
    val scrollState = rememberScrollState()

    val currentError = state.error
    val errorMessage = currentError?.toCommonUIString()

    LaunchedEffect(currentError) {
        if (currentError != null && errorMessage != null) {
            onError(errorMessage)
            presenter.onIntent(MonthIntent.DismissError)
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        if (state.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = AppTheme.colors.primary.toColor()
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = AppTheme.dimens.medium)
            ) {
                // Time Selector
                MonthTimeSelector(
                    formattedMonth = formattedSelectedMonth,
                    canGoBack = state.canGoToPreviousMonth,
                    onPrevious = { presenter.onIntent(MonthIntent.NavigateMonth(false)) },
                    onNext = { presenter.onIntent(MonthIntent.NavigateMonth(true)) }
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .padding(top = AppTheme.dimens.large, bottom = 100.dp),
                    verticalArrangement = Arrangement.spacedBy(AppTheme.dimens.extraLarge)
                ) {

                    // Budget Section
                    StatsCardWallet(
                        activeWalletName = state.activeWalletName,
                        monthlyIncomeInCents = state.monthlyIncomeInCents,
                        disposableIncomeInCents = state.disposableIncomeInCents,
                        onEditBudgetClick = { presenter.onIntent(MonthIntent.OpenEditWalletDialog) }
                    )

                    // Expenses Section
                    if (state.totalOutgoingsInCents > 0) {
                        StatsCardExpense(
                            totalOutgoingsInCents = state.totalOutgoingsInCents,
                            remainingToPayInCents = state.remainingToPayInCents,
                            progress = state.outgoingsProgress
                        )
                    } else {
                        StatsCardEmptyState(
                            onAddOperationClick = onAddOperationClick
                        )
                    }

                    // Upcoming Expenses
                    CardUpcomingExpenses(
                        nextUpcomingExpenses = state.nextUpcomingExpenses,
                        onNavigateToList = onNavigateToList
                    )


                    // Recurrence Breakdown
                    StatsCardRecurrence(
                        breakdown = state.expensesByRecurrence
                    )
                }
            }
        }

        if (state.isEditWalletDialogVisible) {
            WalletEditDialog(
                nameBuffer = state.editWalletNameBuffer,
                amountBuffer = state.editWalletAmountBuffer,
                isValid = state.isEditWalletFormValid,
                onNameChange = { presenter.onIntent(MonthIntent.UpdateEditWalletName(it)) },
                onAmountChange = { presenter.onIntent(MonthIntent.UpdateEditWalletAmount(it)) },
                onDismiss = { presenter.onIntent(MonthIntent.CloseEditWalletDialog) },
                onConfirm = { presenter.onIntent(MonthIntent.SubmitWalletAndIncome) }
            )
        }
    }
}