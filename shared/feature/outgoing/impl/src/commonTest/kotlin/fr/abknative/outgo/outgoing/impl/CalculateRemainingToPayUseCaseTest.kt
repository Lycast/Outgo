package fr.abknative.outgo.outgoing.impl

import fr.abknative.outgo.outgoing.impl.mock.FakeTimeProvider
import fr.abknative.outgo.outgoing.impl.mock.createOutgoing
import fr.abknative.outgo.outgoing.impl.usecase.CalculateRemainingToPayUseCaseImpl
import io.kotest.matchers.shouldBe
import kotlin.test.Test

class CalculateRemainingToPayUseCaseTest {
    private val timeProvider = FakeTimeProvider()
    private val useCase = CalculateRemainingToPayUseCaseImpl(timeProvider)

    @Test
    fun `should return 0 when selected month is in the past`() {
        timeProvider.mockedMonth = 5 // Mai
        val outgoings = listOf(createOutgoing(amount = 1000, dueDay = 10))

        val result = useCase(outgoings, selectedMonth = 4)

        result shouldBe 0L
    }
}