package fr.abknative.outgo.android.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import fr.abknative.outgo.android.components.month.StatsCardExpense
import fr.abknative.outgo.android.components.month.StatsCardRecurrence
import fr.abknative.outgo.android.components.month.StatsCardWallet
import fr.abknative.outgo.android.components.operation.OperationCardSummary
import fr.abknative.outgo.android.designsystem.components.cards.GlassCard
import fr.abknative.outgo.android.designsystem.components.feedback.AppSnackbar
import fr.abknative.outgo.android.designsystem.components.selection.MonthTimeSelector
import fr.abknative.outgo.android.designsystem.foundation.AppTheme
import fr.abknative.outgo.android.designsystem.foundation.toColor
import fr.abknative.outgo.android.ui.extensions.getMonthName
import fr.abknative.outgo.month.api.MonthIntent
import fr.abknative.outgo.month.api.MonthPresenter
import java.util.Locale.getDefault

/**
 * Main screen for displaying monthly financial summaries.
 * Handles the UI state observation, error feedback, and user navigation intents.
 *
 * @param presenter The presentation layer component managing this screen's state and intents.
 * @param modifier Optional modifier to adjust the layout of the root component.
 */
@Composable
fun MonthScreen(
    presenter: MonthPresenter,
    modifier: Modifier = Modifier
) {
    val state by presenter.state.collectAsStateWithLifecycle()
    val formattedSelectedMonth = "${getMonthName(state.selectedMonth)} ${state.selectedYear}".uppercase(getDefault())
    val scrollState = rememberScrollState()

    // State to manage the asynchronous display of Snackbars
    val snackbarHostState = remember { SnackbarHostState() }

    // React to error state changes
    LaunchedEffect(state.error) {
        state.error?.let { appError ->
            // Assume appError provides a readable message.
            val message = appError.message ?: "Une erreur inattendue est survenue"
            snackbarHostState.showSnackbar(
                message = message,
                actionLabel = "OK"
            )
            // Once the snackbar is dismissed or expires, clear the error from the state
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
                    .padding(top = AppTheme.dimens.extraSmall)
            ) {
                // 1. Time Selector
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
                    // 2. Budget Section
                    Column {
                        Text(
                            text = "budget".uppercase(getDefault()),
                            style = AppTheme.typo.label,
                            fontWeight = FontWeight.Bold,
                            color = AppTheme.colors.primary.toColor(),
                            modifier = Modifier.padding(bottom = AppTheme.dimens.small, start = AppTheme.dimens.medium)
                        )
                        GlassCard {
                            StatsCardWallet(
                                activeWalletName = state.activeWalletName,
                                monthlyIncomeInCents = state.monthlyIncomeInCents,
                                totalOutgoingsInCents = state.totalOutgoingsInCents,
                                disposableIncomeInCents = state.disposableIncomeInCents,
                                onEditBudgetClick = {}
                            )
                        }
                    }

                    // 3. Expenses Section
                    Column {
                        Text(
                            text = "dépenses".uppercase(getDefault()),
                            style = AppTheme.typo.label,
                            fontWeight = FontWeight.Bold,
                            color = AppTheme.colors.primary.toColor(),
                            modifier = Modifier.padding(bottom = AppTheme.dimens.small, start = AppTheme.dimens.medium)
                        )
                        GlassCard {
                            StatsCardExpense(
                                totalOutgoingsInCents = state.totalOutgoingsInCents,
                                remainingToPayInCents = state.remainingToPayInCents,
                            )
                        }
                    }

                    // 4. Upcoming Expenses
                    if (state.nextUpcomingExpenses.isNotEmpty()) {
                        Column {
                            Text(
                                text = "Dépenses à venir".uppercase(getDefault()),
                                style = AppTheme.typo.label,
                                fontWeight = FontWeight.Bold,
                                color = AppTheme.colors.primary.toColor(),
                                modifier = Modifier.padding(
                                    bottom = AppTheme.dimens.small,
                                    start = AppTheme.dimens.medium
                                )
                            )
                            GlassCard {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = AppTheme.dimens.medium)
                                ) {
                                    state.nextUpcomingExpenses.forEachIndexed { index, projectedOp ->
                                        val displayOperation = projectedOp.operation.copy(
                                            startDate = projectedOp.projectedDate
                                        )

                                        OperationCardSummary(
                                            operation = displayOperation,
                                            onClick = { /* Read Only */ }
                                        )

                                        if (index < state.nextUpcomingExpenses.lastIndex) {
                                            Spacer(modifier = Modifier.height(AppTheme.dimens.small))
                                            HorizontalDivider(
                                                color = AppTheme.colors.surface100.toColor(),
                                                modifier = Modifier.padding(horizontal = AppTheme.dimens.small)
                                            )
                                            Spacer(modifier = Modifier.height(AppTheme.dimens.small))
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // 5. Recurrence Breakdown
                    if (state.expensesByRecurrence.isNotEmpty()) {
                        Column {
                            Text(
                                text = "Répartition".uppercase(getDefault()),
                                style = AppTheme.typo.label,
                                fontWeight = FontWeight.Bold,
                                color = AppTheme.colors.primary.toColor(),
                                modifier = Modifier.padding(bottom = AppTheme.dimens.small, start = AppTheme.dimens.medium)
                            )
                            GlassCard {
                                StatsCardRecurrence(
                                    breakdown = state.expensesByRecurrence,
                                    monthlyIncome = state.monthlyIncomeInCents
                                )
                            }
                        }
                    }
                }
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 80.dp)
        ) { snackbarData ->
            AppSnackbar(snackbarData = snackbarData)
        }
    }
}