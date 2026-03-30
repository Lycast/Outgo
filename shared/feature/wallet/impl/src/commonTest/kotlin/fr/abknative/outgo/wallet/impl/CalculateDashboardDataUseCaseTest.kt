package fr.abknative.outgo.wallet.impl

import fr.abknative.outgo.wallet.impl.mock.FakeTimeProvider
import fr.abknative.outgo.wallet.impl.mock.createOp
import fr.abknative.outgo.wallet.impl.usecase.CalculateDashboardDataUseCaseImpl
import io.kotest.matchers.shouldBe
import kotlin.test.Test

class CalculateDashboardDataUseCaseTest {
    private val timeProvider = FakeTimeProvider()
    private val useCase = CalculateDashboardDataUseCaseImpl(timeProvider)

    @Test
    fun `current month - should only count future operations in remaining to pay`() {
        timeProvider.mockedDay = 15
        timeProvider.mockedMonth = 3
        timeProvider.mockedYear = 2026

        val ops = listOf(
            createOp(name = "Passé", amount = 100, day = 10),
            createOp(name = "Futur", amount = 200, day = 20)
        )

        val result = useCase(ops, currentMonth = 3, currentYear = 2026)

        result.remainingToPayInCents shouldBe 200L
        result.totalExpensesInCents shouldBe 300L
    }

    @Test
    fun `past month - remaining to pay should always be 0`() {
        timeProvider.mockedMonth = 5 // On est en Mai
        val ops = listOf(createOp(amount = 1000, day = 10))

        val result = useCase(ops, currentMonth = 4, currentYear = 2026) // On regarde Avril

        result.remainingToPayInCents shouldBe 0L
    }
}