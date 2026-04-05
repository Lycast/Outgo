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
        private const val KEY_OFFLINE_USER_ID = "offline_user_id"
    }

    override fun getCurrentUserId(): String {
        // 1. Lecture synchrone et sécurisée grâce au StateFlow
        val currentSession = authRepository.observeSession().value

        if (currentSession != null) {
            return currentSession.userId
        }

        // 2. Fallback sur l'ID hors-ligne
        var offlineId = storage.getString(KEY_OFFLINE_USER_ID)

        if (offlineId == null) {
            offlineId = "local_${Uuid.random()}"
            storage.putString(KEY_OFFLINE_USER_ID, offlineId)
        }

        return offlineId
    }

    override fun observeUserId(): Flow<String> {
        return authRepository.observeSession()
            .map { session -> session?.userId ?: getCurrentUserId() }
            .distinctUntilChanged()
    }
}