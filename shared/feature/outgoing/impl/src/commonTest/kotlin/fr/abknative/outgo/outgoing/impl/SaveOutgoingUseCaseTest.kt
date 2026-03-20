package fr.abknative.outgo.outgoing.impl

import fr.abknative.outgo.core.api.Result
import fr.abknative.outgo.core.api.SyncStatus
import fr.abknative.outgo.outgoing.api.OutgoingError
import fr.abknative.outgo.outgoing.api.Recurrence
import fr.abknative.outgo.outgoing.impl.mock.FakeOutgoingRepository
import fr.abknative.outgo.outgoing.impl.mock.FakeTimeProvider
import fr.abknative.outgo.outgoing.impl.mock.createOutgoing
import fr.abknative.outgo.outgoing.impl.usecase.SaveOutgoingUseCaseImpl
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class SaveOutgoingUseCaseTest {
    private val repository = FakeOutgoingRepository()
    private val timeProvider = FakeTimeProvider()
    private val useCase = SaveOutgoingUseCaseImpl(repository, timeProvider)

    @Test
    fun `should return Error when name is blank`() = runTest {
        val result = useCase(id = null, name = "  ", amountInCents = 100, recurrence = Recurrence.MONTHLY, dueDay = 1)

        result.shouldBeInstanceOf<Result.Error<OutgoingError.EmptyName>>()
    }

    @Test
    fun `should set PENDING_CREATE when inserting a new outgoing`() = runTest {
        val now = 123456789L
        timeProvider.mockedNow = now
        repository.outgoingToReturn = null // On simule que la dépense n'existe pas

        useCase(id = null, name = "Netflix", amountInCents = 1000, recurrence = Recurrence.MONTHLY, dueDay = 5)

        // Au lieu de coVerify, on vérifie l'état de notre Fake
        val saved = repository.lastSavedOutgoing
        saved?.name shouldBe "Netflix"
        saved?.syncStatus shouldBe SyncStatus.PENDING_CREATE
        saved?.createdAt shouldBe now
    }

    @Test
    fun `should set PENDING_UPDATE when updating an already synced outgoing`() = runTest {
        val existing = createOutgoing(id = "123", syncStatus = SyncStatus.SYNCED)
        repository.outgoingToReturn = existing // On simule que la dépense existe déjà
        timeProvider.mockedNow = 999L

        useCase(id = "123", name = "Netflix Ultra", amountInCents = 2000, recurrence = Recurrence.MONTHLY, dueDay = 5)

        val saved = repository.lastSavedOutgoing
        saved?.syncStatus shouldBe SyncStatus.PENDING_UPDATE
        saved?.updatedAt shouldBe 999L
    }
}