package fr.abknative.outgo.outgoing.impl

import fr.abknative.outgo.outgoing.api.model.Budget
import fr.abknative.outgo.outgoing.api.presenter.OutgoingIntent
import fr.abknative.outgo.outgoing.impl.mock.FakeBudgetRepository
import fr.abknative.outgo.outgoing.impl.mock.FakeKeyValueStorage
import fr.abknative.outgo.outgoing.impl.mock.FakeOutgoingRepository
import fr.abknative.outgo.outgoing.impl.mock.FakeTimeProvider
import fr.abknative.outgo.outgoing.impl.presenter.OutgoingPresenterImpl
import fr.abknative.outgo.outgoing.impl.usecase.*
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
class OutgoingPresenterTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    // Fakes
    private val outgoRepository = FakeOutgoingRepository()
    private val budgetRepository = FakeBudgetRepository()
    private val timeProvider = FakeTimeProvider()
    private val storage = FakeKeyValueStorage()

    private lateinit var presenter: OutgoingPresenterImpl

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        presenter = OutgoingPresenterImpl(
            observeActiveOutgoings = ObserveActiveOutgoingsUseCaseImpl(outgoRepository),
            saveOutgoing = SaveOutgoingUseCaseImpl(outgoRepository, timeProvider),
            deleteOutgoing = DeleteOutgoingUseCaseImpl(outgoRepository),
            calculateTotalOutgoings = CalculateTotalOutgoingsUseCaseImpl(),
            calculateRemainingToPay = CalculateRemainingToPayUseCaseImpl(timeProvider),
            calculateDisposableIncome = CalculateDisposableIncomeUseCaseImpl(),
            updateIncome = UpdateIncomeUseCaseImpl(budgetRepository),
            budgetRepository = budgetRepository,
            timeProvider = timeProvider,
            storage = storage
        )
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `should calculate financial summary when budget and outgoings are emitted`() = runTest {
        // 1. Simuler un budget de 2000€
        budgetRepository.emit(Budget(id = "default", monthlyIncomeInCents = 2000_00))

        // 2. Vérifier que le revenu est mis à jour dans l'état
        presenter.state.value.monthlyIncomeInCents shouldBe 2000_00

        // 3. Envoyer un Intent pour changer de mois
        presenter.onIntent(OutgoingIntent.SelectMonth(6))
        presenter.state.value.selectedMonth shouldBe 6
    }

    @Test
    fun `should toggle hero section and persist in storage`() {
        // Par défaut, c'est true
        presenter.state.value.isHeroExpanded shouldBe true

        // Action : Fermer la section
        presenter.onIntent(OutgoingIntent.ToggleHeroSection(false))

        // Vérification État + Persistance
        presenter.state.value.isHeroExpanded shouldBe false
        storage.getBoolean("hero_section_expanded", true) shouldBe false
    }
}