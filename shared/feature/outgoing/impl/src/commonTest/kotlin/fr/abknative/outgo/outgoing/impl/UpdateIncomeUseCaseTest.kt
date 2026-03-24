package fr.abknative.outgo.outgoing.impl

import fr.abknative.outgo.core.api.Result
import fr.abknative.outgo.core.api.SyncStatus
import fr.abknative.outgo.outgoing.api.model.Budget
import fr.abknative.outgo.outgoing.impl.mock.FakeBudgetRepository
import fr.abknative.outgo.outgoing.impl.mock.FakeTimeProvider
import fr.abknative.outgo.outgoing.impl.usecase.UpdateIncomeUseCaseImpl
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class UpdateIncomeUseCaseTest {

    private val repository = FakeBudgetRepository()
    private val timeProvider = FakeTimeProvider()
    private val useCase = UpdateIncomeUseCaseImpl(repository, timeProvider)

    @Test
    fun `should insert new budget when none exists`() = runTest {
        repository.emit(null)
        timeProvider.mockedNow = 12345L // On fixe une heure précise

        val result = useCase(amountInCents = 1500_00, budgetId = "default")

        result.shouldBeInstanceOf<Result.Success<Unit>>()
        val inserted = repository.lastInsertedBudget
        inserted?.monthlyIncomeInCents shouldBe 1500_00
        inserted?.syncStatus shouldBe SyncStatus.PENDING_CREATE
        inserted?.updatedAt shouldBe 12345L
    }

    @Test
    fun `should update existing budget when it already exists`() = runTest {
        // État initial : un budget existe déjà
        repository.emit(Budget(id = "default", monthlyIncomeInCents = 1000_00, createdAt = 0, updatedAt = 0))

        val result = useCase(amountInCents = 2000_00, budgetId = "default")

        result.shouldBeInstanceOf<Result.Success<Unit>>()
        repository.lastUpdatedBudget?.monthlyIncomeInCents shouldBe 2000_00
    }

    @Test
    fun `should return error when repository fails`() = runTest {
        // Simulation d'une erreur base de données
        repository.shouldReturnError = true

        val result = useCase(amountInCents = 1000_00, budgetId = "default")

        result.shouldBeInstanceOf<Result.Error<*>>()
    }
}