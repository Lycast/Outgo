package fr.abknative.outgo.wallet.impl.usecase.engine

import fr.abknative.outgo.wallet.api.model.dashboard.DashboardData
import fr.abknative.outgo.wallet.api.model.dashboard.ProjectedOperation

internal interface DashboardCalculationEngine {
    /**
     * Calculates the dashboard metrics using projected occurrences
     * instead of raw rules to ensure temporal accuracy.
     */
    fun calculate(
        operations: List<ProjectedOperation>,
        currentMonth: Int,
        currentYear: Int
    ): DashboardData
}