package fr.abknative.outgo.android.components.dashboard

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import fr.abknative.outgo.android.designsystem.components.cards.GlassCard
import fr.abknative.outgo.android.designsystem.foundation.AppTheme
import fr.abknative.outgo.android.designsystem.foundation.toColor
import kotlinx.coroutines.launch

/**
 * The main Dashboard header. Displays monthly budget summaries, income vs outgoings,
 * and a month navigator within a GlassCard.
 */
@Composable
fun HeroSection(
    isExpanded: Boolean,
    canGoToPreviousMonth: Boolean,
    formattedMonthDate: String,
    activeWalletName: String,
    monthlyIncomeInCents: Long,
    totalOutgoingsInCents: Long,
    disposableIncomeInCents: Long,
    remainingToPayInCents: Long,
    onToggleExpand: () -> Unit,
    onEditBudgetClick: () -> Unit,
    onPreviousMonthClick: () -> Unit,
    onNextMonthClick: () -> Unit
) {

    val haptic = LocalHapticFeedback.current

    val scope = rememberCoroutineScope()
    val pageCount = 2
    val pagerState = rememberPagerState(pageCount = { pageCount })

    LaunchedEffect(isExpanded) {
        if (isExpanded) haptic.performHapticFeedback(HapticFeedbackType.LongPress)
    }

    Box(
        modifier = Modifier.fillMaxWidth().padding(horizontal = AppTheme.dimens.medium)
    ) {
        GlassCard {
            Column(modifier = Modifier.fillMaxWidth()) {

                // Static Header
                MonthBudgetSelector(
                    formattedMonthDate = formattedMonthDate,
                    canGoToPreviousMonth = canGoToPreviousMonth,
                    onPreviousMonthClick = onPreviousMonthClick,
                    onNextMonthClick = onNextMonthClick
                )

                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = AppTheme.dimens.large),
                    color = AppTheme.colors.textSecondary.toColor().copy(alpha = 0.1f)
                )

                AnimatedVisibility(
                    visible = isExpanded,
                    enter = expandVertically() + fadeIn(),
                    exit = shrinkVertically() + fadeOut(),
                ) {

                    // Carousel Content
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier.fillMaxWidth().height(220.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) { pageIndex ->
                        when (pageIndex) {
                            0 -> HeroGlobalContent(
                                activeWalletName = activeWalletName,
                                monthlyIncomeInCents = monthlyIncomeInCents,
                                totalOutgoingsInCents = totalOutgoingsInCents,
                                disposableIncomeInCents = disposableIncomeInCents,
                                remainingToPayInCents = remainingToPayInCents,
                                onEditBudgetClick = onEditBudgetClick
                            )

                            1 -> HeroExpenseContent(
                                totalOutgoingsInCents = totalOutgoingsInCents,
                                remainingToPayInCents = remainingToPayInCents
                            )
                        }
                    }

                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = AppTheme.dimens.large),
                        color = AppTheme.colors.textSecondary.toColor().copy(alpha = 0.1f)
                    )
                }

                HeroFooter(
                    isExpanded = isExpanded,
                    onToggleExpand = onToggleExpand,
                    onPreviousPage = {
                        scope.launch {
                            val prevPage = (pagerState.currentPage - 1 + pageCount) % pageCount
                            pagerState.animateScrollToPage(prevPage)
                        }
                    },
                    onNextPage = {
                        scope.launch {
                            val nextPage = (pagerState.currentPage + 1) % pageCount
                            pagerState.animateScrollToPage(nextPage)
                        }
                    }
                )
            }
        }
    }
}