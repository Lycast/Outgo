package fr.abknative.outgo.outgoing.api.repository

import fr.abknative.outgo.core.api.AppException
import fr.abknative.outgo.core.api.Result
import fr.abknative.outgo.outgoing.api.model.Outgoing
import kotlinx.coroutines.flow.Flow

/**
 * Single source of truth for managing subscription data.
 *
 * This repository abstracts the underlying data sources (local database or remote API),
 * providing a unified interface for the domain layer.
 */
interface OutgoingRepository {

    /**
     * Provides a reactive stream of all active subscriptions.
     * * The flow emits a new list whenever the underlying data changes,
     * excluding records marked as deleted.
     */
    fun observeActiveOutgoings(): Flow<List<Outgoing>>

    /**
     * Récupère une dépense spécifique par son identifiant unique.
     * Retourne null si aucune dépense n'est trouvée.
     */
    suspend fun getOutgoingById(id: String): Outgoing?

    /**
     * Persists a new subscription or updates an existing one.
     */
    suspend fun upsert(outgoing: Outgoing): Result<Unit, AppException>

    /**
     * Performs a logical deletion of a subscription.
     * * The record remains in the local storage but is marked for future
     * synchronization with the remote backend.
     */
    suspend fun markAsDeleted(id: String): Result<Unit, AppException>
}