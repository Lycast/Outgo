package fr.abknative.outgo.auth.impl.repository

import fr.abknative.outgo.auth.api.model.UserSession
import fr.abknative.outgo.auth.api.repository.AuthRepository
import fr.abknative.outgo.core.api.KeyValueStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

internal class AuthRepositoryImpl(
    private val storage: KeyValueStorage
) : AuthRepository {
    private val _session = MutableStateFlow<UserSession?>(loadSession())

    override fun observeSession(): Flow<UserSession?> = _session.asStateFlow()

    override suspend fun getSession(): UserSession? {
        return _session.value
    }

    override suspend fun login() {
        val mockSession = UserSession(userId = "user_debug_123", token = "debug", email = "test@outgo.app")
        saveSession(mockSession)
        _session.value = mockSession
    }

    override suspend fun logout() {
        storage.remove("auth_user_id")
        storage.remove("auth_token")
        _session.value = null
    }

    private fun loadSession(): UserSession? {
        val userId = storage.getString("auth_user_id") ?: return null
        val token = storage.getString("auth_token") ?: return null
        return UserSession(userId, token, "test@outgo.app")
    }

    private fun saveSession(session: UserSession) {
        storage.putString("auth_user_id", session.userId)
        storage.putString("auth_token", session.token)
    }
}