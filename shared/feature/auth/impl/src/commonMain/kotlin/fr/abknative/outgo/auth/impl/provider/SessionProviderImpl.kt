package fr.abknative.outgo.auth.impl.provider

import fr.abknative.outgo.auth.api.provider.SessionProvider
import fr.abknative.outgo.auth.api.repository.AuthRepository
import fr.abknative.outgo.core.api.KeyValueStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
internal class SessionProviderImpl(
    private val authRepository: AuthRepository,
    private val storage: KeyValueStorage
) : SessionProvider {

    companion object {
        private const val KEY_PERSISTENT_USER_ID = "persistent_user_id"
    }

    /**
     * Retrieves the active user ID.
     * Stores the authenticated Firebase UID as the persistent ID if a session is active.
     * If no session is active (e.g., after logout), it falls back to the last known
     * persistent ID to keep data visible offline. Generates a local ID on first launch.
     */
    override fun getCurrentUserId(): String {
        val currentSession = authRepository.observeSession().value

        if (currentSession != null) {
            storage.putString(KEY_PERSISTENT_USER_ID, currentSession.userId)
            return currentSession.userId
        }

        var lastId = storage.getString(KEY_PERSISTENT_USER_ID)

        if (lastId == null) {
            lastId = "local_${Uuid.random()}"
            storage.putString(KEY_PERSISTENT_USER_ID, lastId)
        }

        return lastId
    }

    /**
     * Observes the user ID changes.
     */
    override fun observeUserId(): Flow<String> {
        return authRepository.observeSession()
            .map { session -> session?.userId ?: getCurrentUserId() }
            .distinctUntilChanged()
    }
}