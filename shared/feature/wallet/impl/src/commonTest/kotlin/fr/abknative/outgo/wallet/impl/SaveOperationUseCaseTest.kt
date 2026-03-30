package fr.abknative.outgo.wallet.impl

import fr.abknative.outgo.core.api.logs.Result
import fr.abknative.outgo.wallet.api.OperationError
import fr.abknative.outgo.wallet.api.model.OperationType
import fr.abknative.outgo.wallet.api.model.Recurrence
import fr.abknative.outgo.wallet.impl.mock.FakeOperationRepository
import fr.abknative.outgo.wallet.impl.usecase.SaveOperationUseCaseImpl
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class SaveOperationUseCaseTest {
    private val repository = FakeOperationRepository()
    private val useCase = SaveOperationUseCaseImpl(repository)

    @Test
    fun `should return Error when name is blank`() = runTest {
        val result = useCase(
            walletId = "123",
            name = "  ",
            amountInCents = 100,
            type = OperationType.EXPENSE,
            recurrence = Recurrence.UNIQUE,
            startDate = 1000L
        )

        result.shouldBeInstanceOf<Result.Error<OperationError.EmptyName>>()
    }

    @Test
    fun `should call repository save with correct data`() = runTest {
        useCase(
            walletId = "wallet_01",
            name = "Netflix",
            amountInCents = 1500,
            type = OperationType.EXPENSE,
            recurrence = Recurrence.MONTHLY,
            startDate = 1000L
        )

        repository.lastSavedOperation?.name shouldBe "Netflix"
        repository.lastSavedOperation?.walletId shouldBe "wallet_01"
    }
}