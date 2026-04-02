package fr.abknative.outgo.wallet.api.usecase

import fr.abknative.outgo.wallet.api.model.dashboard.DashboardData
import fr.abknative.outgo.wallet.api.model.dashboard.ProjectedOperation

/**
 * Encapsulates the core financial algorithms.
 * Processes projected operations into unified [DashboardData].
 */
interface CalculateDashboardDataUseCase {
    /**
     * Computes the financial metrics for the requested period based on occurrences.
     *
     * @param operations The list of projected operations (occurrences) to process.
     * @param currentMonth The month currently viewed by the user.
     * @param currentYear The year currently viewed by the user.
     * @return The aggregated [DashboardData] ready for UI consumption.
     */
    operator fun invoke(
        operations: List<ProjectedOperation>,
        currentMonth: Int,
        currentYear: Int
    ): DashboardData
}