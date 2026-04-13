package fr.abknative.outgo.wallet.impl.usecase

import fr.abknative.outgo.subscription.api.FeatureManager
import fr.abknative.outgo.wallet.api.model.presenter.PeriodStats
import fr.abknative.outgo.wallet.api.model.presenter.ProjectedOperation
import fr.abknative.outgo.wallet.api.usecase.CalculateDashboardDataUseCase
import fr.abknative.outgo.wallet.impl.usecase.engine.SimpleSumEngine
import fr.abknative.outgo.wallet.impl.usecase.engine.TimelineEngine

internal class CalculateDashboardDataUseCaseImpl(
    private val featureManager: FeatureManager,
    private val simpleSumEngine: SimpleSumEngine,
    private val timelineEngine: TimelineEngine
) : CalculateDashboardDataUseCase {

    override fun invoke(
        operations: List<ProjectedOperation>,
        currentMonth: Int,
        currentYear: Int
    ): PeriodStats {

        val isPremiumUser = featureManager.isPremium()
        val engine = if (isPremiumUser) { timelineEngine } else { simpleSumEngine }

        return engine.calculate(operations, currentMonth, currentYear)
    }
}