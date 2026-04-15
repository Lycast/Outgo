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
import fr.abknative.outgo.android.components.month.*
import fr.abknative.outgo.android.designsystem.components.cards.GlassCard
import fr.abknative.outgo.android.designsystem.components.feedback.AppSnackbar
import fr.abknative.outgo.android.designsystem.components.selection.MonthTimeSelector
import fr.abknative.outgo.android.designsystem.foundation.AppTheme
import fr.abknative.outgo.android.designsystem.foundation.toColor
import fr.abknative.outgo.android.ui.CommonLabels
import fr.abknative.outgo.android.ui.MonthLabels
import fr.abknative.outgo.android.ui.extensions.getMonthName
import fr.abknative.outgo.month.api.MonthIntent
import fr.abknative.outgo.month.api.MonthPresenter
import java.util.Locale.getDefault

@Composable
fun MonthScreen(
    presenter: MonthPresenter,
    modifier: Modifier = Modifier
) {

    val state by presenter.state.collectAsStateWithLifecycle()
    val formattedSelectedMonth = "${getMonthName(state.selectedMonth)} ${state.selectedYear}".uppercase(getDefault())
    val scrollState = rememberScrollState()
    val snackbarHostState = remember { SnackbarHostState() }
    val defaultErrorMsg = MonthLabels.DEFAULT_ERROR
    val actionOkLabel = CommonLabels.ACTION_OK

    LaunchedEffect(state.error) {
        state.error?.let { appError ->
            val message = appError.message ?: defaultErrorMsg
            snackbarHostState.showSnackbar(
                message = message,
                actionLabel = actionOkLabel
            )
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
                    Column {
                        Text(
                            text = MonthLabels.SECTION_BUDGET.uppercase(getDefault()),
                            style = AppTheme.typo.label,
                            fontWeight = FontWeight.Bold,
                            color = AppTheme.colors.primary.toColor(),
                            modifier = Modifier.padding(bottom = AppTheme.dimens.small, start = AppTheme.dimens.medium)
                        )
                        GlassCard {
                            StatsCardWallet(
                                activeWalletName = state.activeWalletName,
                                monthlyIncomeInCents = state.monthlyIncomeInCents,
                                disposableIncomeInCents = state.disposableIncomeInCents,
                                onEditBudgetClick = { presenter.onIntent(MonthIntent.OpenEditWalletDialog) }
                            )
                        }
                    }

                    // Expenses Section
                    Column {
                        Text(
                            text = MonthLabels.SECTION_EXPENSES.uppercase(getDefault()),
                            style = AppTheme.typo.label,
                            fontWeight = FontWeight.Bold,
                            color = AppTheme.colors.primary.toColor(),
                            modifier = Modifier.padding(bottom = AppTheme.dimens.small, start = AppTheme.dimens.medium)
                        )
                        GlassCard {
                            StatsCardExpense(
                                totalOutgoingsInCents = state.totalOutgoingsInCents,
                                remainingToPayInCents = state.remainingToPayInCents,
                                progress = state.outgoingsProgress
                            )
                        }
                    }

                    // Upcoming Expenses
                    if (state.nextUpcomingExpenses.isNotEmpty()) {
                        Column {
                            Text(
                                text = MonthLabels.SECTION_UPCOMING.uppercase(getDefault()),
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
                                            formattedDate = projectedOp.formattedDate
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

                    // Recurrence Breakdown
                    if (state.expensesByRecurrence.isNotEmpty()) {
                        Column {
                            Text(
                                text = MonthLabels.SECTION_RECURRENCE.uppercase(getDefault()),
                                style = AppTheme.typo.label,
                                fontWeight = FontWeight.Bold,
                                color = AppTheme.colors.primary.toColor(),
                                modifier = Modifier.padding(bottom = AppTheme.dimens.small, start = AppTheme.dimens.medium)
                            )
                            GlassCard {
                                StatsCardRecurrence(
                                    breakdown = state.expensesByRecurrence
                                )
                            }
                        }
                    }
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