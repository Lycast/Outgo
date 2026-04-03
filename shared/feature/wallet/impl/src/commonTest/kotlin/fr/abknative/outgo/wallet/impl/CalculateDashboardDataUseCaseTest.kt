package fr.abknative.outgo.wallet.impl

import fr.abknative.outgo.subscription.api.FeatureManager
import fr.abknative.outgo.wallet.api.model.Operation
import fr.abknative.outgo.wallet.api.model.dashboard.ProjectedOperation
import fr.abknative.outgo.wallet.impl.mock.FakeTimeProvider
import fr.abknative.outgo.wallet.impl.mock.createOp
import fr.abknative.outgo.wallet.impl.usecase.CalculateDashboardDataUseCaseImpl
import fr.abknative.outgo.wallet.impl.usecase.engine.SimpleSumEngine
import fr.abknative.outgo.wallet.impl.usecase.engine.TimelineEngine
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.test.Test

// Helper pour transformer rapidement une Op de test en Projection
fun Operation.asProjected() = ProjectedOperation(
    operation = this,
    projectedDate = this.startDate
)

class FakeFeatureManager(var isPremiumMock: Boolean = false) : FeatureManager {
    override val isPremiumFlow: StateFlow<Boolean> = MutableStateFlow(isPremiumMock)
    override fun isPremium(): Boolean = isPremiumMock
    override fun updatePremiumStatus(untilTimestamp: Long) {}
}

class CalculateDashboardDataUseCaseTest {

    private val timeProvider = FakeTimeProvider()
    private val simpleSumEngine = SimpleSumEngine(timeProvider)
    private val timelineEngine = TimelineEngine(timeProvider)
    private val fakeFeatureManager = FakeFeatureManager(isPremiumMock = false)

    private val useCase = CalculateDashboardDataUseCaseImpl(
        featureManager = fakeFeatureManager,
        simpleSumEngine = simpleSumEngine,
        timelineEngine = timelineEngine
    )

    @Test
    fun `current month - should only count future operations in remaining to pay`() {
        timeProvider.mockedDay = 15
        timeProvider.mockedMonth = 3
        timeProvider.mockedYear = 2026

        // On crée les opérations et on les projette immédiatement
        val ops = listOf(
            createOp(name = "Passé", amount = 100, day = 10).asProjected(),
            createOp(name = "Futur", amount = 200, day = 20).asProjected()
        )

        val result = useCase(ops, currentMonth = 3, currentYear = 2026)

        result.remainingToPayInCents shouldBe 200L
        result.totalExpensesInCents shouldBe 300L
    }

    @Test
    fun `past month - remaining to pay should always be 0`() {
        timeProvider.mockedMonth = 5
        val ops = listOf(createOp(amount = 1000, day = 10).asProjected())

        val result = useCase(ops, currentMonth = 4, currentYear = 2026)

        result.remainingToPayInCents shouldBe 0L
    }
}