package fr.abknative.outgo.android.components.month

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import fr.abknative.outgo.android.designsystem.components.selection.MonthTimeSelector
import fr.abknative.outgo.android.designsystem.foundation.AppTheme

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

    val actualPageCount = 2
    val virtualPageCount = actualPageCount * 100

    val pagerState = rememberPagerState(
        initialPage = virtualPageCount / 2,
        pageCount = { virtualPageCount }
    )

    LaunchedEffect(isExpanded) {
        if (isExpanded) haptic.performHapticFeedback(HapticFeedbackType.LongPress)
    }

    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = AppTheme.dimens.medium)) {

        MonthTimeSelector(
            formattedMonth = formattedMonthDate,
            canGoBack = canGoToPreviousMonth,
            onPrevious = onPreviousMonthClick,
            onNext = onNextMonthClick
        )
    }
}