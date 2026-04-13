package fr.abknative.outgo.android.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import fr.abknative.outgo.android.components.month.HeroExpenseContent
import fr.abknative.outgo.android.components.month.HeroGlobalContent
import fr.abknative.outgo.android.components.month.MonthRecurrenceBreakdown
import fr.abknative.outgo.android.components.operation.OperationCardSummary
import fr.abknative.outgo.android.designsystem.components.cards.GlassCard
import fr.abknative.outgo.android.designsystem.components.selection.MonthTimeSelector
import fr.abknative.outgo.android.designsystem.foundation.AppTheme
import fr.abknative.outgo.android.designsystem.foundation.toColor
import fr.abknative.outgo.android.ui.extensions.getMonthName
import fr.abknative.outgo.month.api.MonthIntent
import fr.abknative.outgo.month.api.MonthPresenter
import java.util.Locale.getDefault

@Composable
fun MonthScreen(
    presenter: MonthPresenter,
    isPremium: Boolean,
    modifier: Modifier = Modifier
) {
    val state by presenter.state.collectAsStateWithLifecycle()
    val formattedSelectedMonth = "${getMonthName(state.selectedMonth)} ${state.selectedYear}".uppercase()

    // État du scroll pour la Column
    val scrollState = rememberScrollState()

    Box(modifier = modifier.fillMaxSize()) {
        if (state.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else {
            // Un Column standard avec verticalScroll remplace avantageusement le LazyColumn
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = AppTheme.dimens.large)
                    .verticalScroll(scrollState)
                    // On garde le padding en haut et l'espace pour la BottomBar en bas
                    .padding(top = AppTheme.dimens.medium, bottom = 100.dp),
                verticalArrangement = Arrangement.spacedBy(AppTheme.dimens.large)
            ) {

                // 1. Le Sélecteur de temps
                MonthTimeSelector(
                    formattedMonth = formattedSelectedMonth,
                    canGoBack = state.canGoToPreviousMonth,
                    onPrevious = { presenter.onIntent(MonthIntent.NavigateMonth(false)) },
                    onNext = { presenter.onIntent(MonthIntent.NavigateMonth(true)) }
                )

                // 2. Le Bloc Budget
                Column {
                    Text(
                        text = "Vue sur le budget".uppercase(getDefault()),
                        style = AppTheme.typo.caption,
                        fontWeight = FontWeight.Bold,
                        color = AppTheme.colors.primary.toColor(),
                        modifier = Modifier.padding(bottom = AppTheme.dimens.small, start = AppTheme.dimens.medium)
                    )

                    GlassCard {
                        HeroGlobalContent(
                            activeWalletName = state.activeWalletName,
                            monthlyIncomeInCents = state.monthlyIncomeInCents,
                            totalOutgoingsInCents = state.totalOutgoingsInCents,
                            disposableIncomeInCents = state.disposableIncomeInCents,
                            onEditBudgetClick = {
                                // Dans la vue historique du mois, on peut désactiver l'édition du budget
                                // ou rediriger vers l'édition si le Presenter le permet plus tard.
                            }
                        )
                    }
                }

                // 2. Le Bloc Dépenses
                Column {
                    Text(
                        text = "Vue sur les dépenses".uppercase(getDefault()),
                        style = AppTheme.typo.caption,
                        fontWeight = FontWeight.Bold,
                        color = AppTheme.colors.primary.toColor(),
                        modifier = Modifier.padding(bottom = AppTheme.dimens.small, start = AppTheme.dimens.medium)
                    )

                    GlassCard {
                        HeroExpenseContent(
                            totalOutgoingsInCents = state.totalOutgoingsInCents,
                            remainingToPayInCents = state.remainingToPayInCents,
                        )
                    }
                }

                // 3. Répartition par type de dépense
                if (state.expensesByRecurrence.isNotEmpty()) {
                    MonthRecurrenceBreakdown(breakdown = state.expensesByRecurrence)
                }

                // 4. Les prochaines échéances
                if (state.nextUpcomingExpenses.isNotEmpty()) {
                    Column {
                        Text(
                            text = "Dépenses à venir".uppercase(getDefault()),
                            style = AppTheme.typo.caption,
                            fontWeight = FontWeight.Bold,
                            color = AppTheme.colors.primary.toColor(),
                            modifier = Modifier.padding(bottom = AppTheme.dimens.small, start = AppTheme.dimens.medium)
                        )

                        GlassCard {
                            Column(modifier = Modifier.fillMaxWidth().padding(vertical = AppTheme.dimens.medium)) {
                                state.nextUpcomingExpenses.forEachIndexed { index, projectedOp ->
                                    val displayOperation = projectedOp.operation.copy(
                                        startDate = projectedOp.projectedDate
                                    )

                                    OperationCardSummary(
                                        operation = displayOperation,
                                        onClick = { /* Read Only */ }
                                    )

                                    HorizontalDivider(color = AppTheme.colors.textSecondary.toColor().copy(alpha = 0.1f))

                                    if (index < state.nextUpcomingExpenses.lastIndex) {
                                        Spacer(modifier = Modifier.height(AppTheme.dimens.small))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}