package fr.abknative.outgo.outgoing.api.repository

import fr.abknative.outgo.core.api.AppException
import fr.abknative.outgo.core.api.Result
import fr.abknative.outgo.outgoing.api.model.Outgoing
import kotlinx.coroutines.flow.Flow

/**
 * Single source of truth for managing outgoing data.
 *
 * This repository abstracts the underlying data sources (local database or remote API),
 * providing a unified interface for the domain layer.
 */
interface OutgoingRepository {

    /**
     * Provides a reactive stream of all outgoing by month.
     * * The flow emits a new list whenever the underlying data changes,
     * excluding records marked as deleted.
     */
    fun observeOutgoingsByMonth(month: Int): Flow<List<Outgoing>>

    /**
     * Récupère une dépense spécifique par son identifiant unique.
     * Retourne null si aucune dépense n'est trouvée.
     */
    suspend fun getOutgoingById(id: String): Outgoing?

    /**
     * Inserts a new subscription. Fails if the ID already exists.
     */
    suspend fun insert(outgoing: Outgoing): Result<Unit, AppException>

    /**
     * Updates an existing subscription by its ID.
     */
    suspend fun update(outgoing: Outgoing): Result<Unit, AppException>

    /**
     * Performs a logical deletion of a outgoing.
     * * The record remains in the local storage but is marked for future
     * synchronization with the remote backend.
     */
    suspend fun markAsDeleted(id: String): Result<Unit, AppException>
}