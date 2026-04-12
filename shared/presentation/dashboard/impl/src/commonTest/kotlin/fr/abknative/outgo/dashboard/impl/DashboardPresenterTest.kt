package fr.abknative.outgo.dashboard.impl

import fr.abknative.outgo.dashboard.api.DashboardIntent
import fr.abknative.outgo.dashboard.impl.mock.*
import fr.abknative.outgo.wallet.api.model.Wallet
import fr.abknative.outgo.wallet.api.model.dashboard.DashboardData
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

/**
 * Test suite for [DashboardPresenterImpl].
 * Ensures the reactive pipeline correctly maps domain data to UI state.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class DashboardPresenterTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private val timeProvider = FakeTimeProvider()
    private val storage = FakeKeyValueStorage()

    private val mapper = DashboardStateMapper(timeProvider)

    private val observeWallets = FakeObserveWalletsUseCase()
    private val observeActiveOperations = FakeObserveActiveOperationsUseCase()
    private val saveOperation = FakeSaveOperationUseCase()
    private val deleteOperation = FakeDeleteOperationUseCase()
    private val calculateDashboardData = FakeCalculateDashboardDataUseCase()
    private val saveWalletUseCase = FakeSaveWalletUseCase()

    private lateinit var presenter: DashboardPresenterImpl

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        presenter = DashboardPresenterImpl(
            observeActiveOperations = observeActiveOperations,
            observeWallets = observeWallets,
            saveOperation = saveOperation,
            deleteOperation = deleteOperation,
            calculateDashboardData = calculateDashboardData,
            saveWallet = saveWalletUseCase,
            mapper = mapper,
            timeProvider = timeProvider,
            storage = storage
        )
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `should map router calculated data to state`() = runTest {
        observeWallets.emit(listOf(Wallet(id = "w1", name = "Test", createdAt = 0, updatedAt = 0)))

        calculateDashboardData.dataToReturn = DashboardData(
            currentBalanceInCents = 2000_00,
            totalExpensesInCents = 1000_00,
            remainingToPayInCents = 500_00,
            disposableIncomeInCents = 2000_00
        )

        presenter.onIntent(DashboardIntent.SelectMonth(8, 2026))

        presenter.state.value.totalOutgoingsInCents shouldBe 1000_00
        presenter.state.value.remainingToPayInCents shouldBe 500_00
        presenter.state.value.disposableIncomeInCents shouldBe 2000_00
        presenter.state.value.selectedMonth shouldBe 8
    }
}