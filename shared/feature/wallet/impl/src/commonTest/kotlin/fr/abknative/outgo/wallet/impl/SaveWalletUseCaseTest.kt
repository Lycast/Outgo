package fr.abknative.outgo.wallet.impl


import fr.abknative.outgo.core.api.logs.Result
import fr.abknative.outgo.wallet.impl.mock.FakeWalletRepository
import fr.abknative.outgo.wallet.impl.usecase.SaveWalletUseCaseImpl
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class SaveWalletUseCaseTest {
    private val repository = FakeWalletRepository()
    private val useCase = SaveWalletUseCaseImpl(repository)

    @Test
    fun `should call repository save when name is valid`() = runTest {
        val result = useCase(id = null, name = "Compte Courant")

        result.shouldBeInstanceOf<Result.Success<Unit>>()
        repository.lastSavedWallet?.name shouldBe "Compte Courant"
    }
}