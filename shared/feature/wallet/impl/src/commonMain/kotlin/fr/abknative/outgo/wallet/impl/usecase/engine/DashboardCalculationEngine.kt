package fr.abknative.outgo.wallet.impl.usecase.engine

import fr.abknative.outgo.wallet.api.model.presenter.PeriodStats
import fr.abknative.outgo.wallet.api.model.presenter.ProjectedOperation

internal interface DashboardCalculationEngine {
    /**
     * Calculates the dashboard metrics using projected occurrences
     * instead of raw rules to ensure temporal accuracy.
     */
    fun calculate(
        operations: List<ProjectedOperation>,
        currentMonth: Int,
        currentYear: Int
    ): PeriodStats
}