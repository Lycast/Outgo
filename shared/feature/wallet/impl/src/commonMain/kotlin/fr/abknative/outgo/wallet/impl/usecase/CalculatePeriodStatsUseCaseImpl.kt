package fr.abknative.outgo.wallet.impl.usecase

import fr.abknative.outgo.subscription.api.FeatureManager
import fr.abknative.outgo.wallet.api.model.presenter.PeriodStats
import fr.abknative.outgo.wallet.api.model.presenter.ProjectedOperation
import fr.abknative.outgo.wallet.api.usecase.CalculatePeriodStatsUseCase
import fr.abknative.outgo.wallet.impl.usecase.engine.SimplePeriodStatsCalculation
import fr.abknative.outgo.wallet.impl.usecase.engine.TimelinePeriodStatsCalculation

internal class CalculatePeriodStatsUseCaseImpl(
    private val featureManager: FeatureManager,
    private val simplePeriodStatsCalculationEngine: SimplePeriodStatsCalculation,
    private val timelinePeriodStatsCalculationEngine: TimelinePeriodStatsCalculation
) : CalculatePeriodStatsUseCase {

    override fun invoke(
        operations: List<ProjectedOperation>,
        currentMonth: Int,
        currentYear: Int
    ): PeriodStats {

        val isPremiumUser = featureManager.isPremium()
        val engine = if (isPremiumUser) { timelinePeriodStatsCalculationEngine } else { simplePeriodStatsCalculationEngine }

        return engine.calculate(operations, currentMonth, currentYear)
    }
}