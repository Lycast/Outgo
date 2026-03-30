package fr.abknative.outgo.dashboard.impl

import fr.abknative.outgo.auth.api.usecase.ObserveUserSessionUseCase
import fr.abknative.outgo.dashboard.api.DashboardIntent
import fr.abknative.outgo.dashboard.impl.mock.*
import fr.abknative.outgo.wallet.api.model.Wallet
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DashboardPresenterTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private val timeProvider = FakeTimeProvider()
    private val syncManager = FakeSyncManager()
    private val storage = FakeKeyValueStorage()
    private val budgetRepository = FakeWalletRepository()
    private val authRepository = FakeAuthRepository()
    private val observeActiveOutgoings = FakeObserveActiveOperationsUseCase()
    private val saveOutgoing = FakeSaveOperationUseCase()
    private val deleteOutgoing = FakeDeleteOperationUseCase()
    private val calculateTotalOutgoings = FakeCalculateTotalExpensesUseCase()
    private val calculateRemainingToPay = FakeCalculateRemainingToPayUseCase()
    private val calculateDisposableIncome = FakeCalculateDisposableIncomeUseCase()
    private val updateIncome = FakeUpdateIncomeUseCase()

    private lateinit var presenter: DashboardPresenterImpl

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        val fakeObserveUserSession = object : ObserveUserSessionUseCase {
            override fun invoke() = authRepository.observeSession()
        }

        presenter = DashboardPresenterImpl(
            observeActiveOutgoings = observeActiveOutgoings,
            observeUserSession = fakeObserveUserSession,
            saveOutgoing = saveOutgoing,
            deleteOutgoing = deleteOutgoing,
            calculateTotalOutgoings = calculateTotalOutgoings,
            calculateRemainingToPay = calculateRemainingToPay,
            calculateDisposableIncome = calculateDisposableIncome,
            updateIncome = updateIncome,
            walletRepository = budgetRepository,
            timeProvider = timeProvider,
            syncManager = syncManager,
            storage = storage
        )
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `should map calculated usecases data to state`() = runTest {
        budgetRepository.emit(Wallet(id = "default", monthlyIncomeInCents = 3000_00, createdAt = 0, updatedAt = 0))
        calculateTotalOutgoings.totalToReturn = 1000_00
        calculateRemainingToPay.remainingToReturn = 500_00
        calculateDisposableIncome.disposableToReturn = 2000_00

        presenter.onIntent(DashboardIntent.SelectMonth(8))

        presenter.state.value.monthlyIncomeInCents shouldBe 3000_00
        presenter.state.value.totalOutgoingsInCents shouldBe 1000_00
        presenter.state.value.remainingToPayInCents shouldBe 500_00
        presenter.state.value.disposableIncomeInCents shouldBe 2000_00
        presenter.state.value.selectedMonth shouldBe 8
    }
}