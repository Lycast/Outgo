package fr.abknative.outgo.auth.impl.repository

import fr.abknative.outgo.auth.api.AuthError
import fr.abknative.outgo.auth.api.model.UserSession
import fr.abknative.outgo.auth.api.repository.AuthRepository
import fr.abknative.outgo.core.api.KeyValueStorage
import fr.abknative.outgo.core.api.logs.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.time.Duration.Companion.milliseconds

internal class AuthRepositoryImpl(
    private val storage: KeyValueStorage
) : AuthRepository {

    companion object {
        private const val KEY_USER_ID = "auth_user_id"
        private const val KEY_TOKEN = "auth_token"
        private const val KEY_EMAIL = "auth_email"
        private const val TAG = "AuthLocalRepo"
    }

    private val sessionState = MutableStateFlow(loadSessionFromStorage())

    override fun observeSession(): Flow<UserSession?> = sessionState.asStateFlow()

    override suspend fun getSession(): UserSession? = sessionState.value

    override suspend fun login(email: String, password: String): Result<Unit, AppException> = asResult(
        onError = {
            AppLogger.get()?.e(TAG, "Login failed for email: $email", it)
            it as? AppException ?: CommonError.UnknownError(it)
        }
    ) {
        delay(1000.milliseconds)

        if (email.isBlank() || password.isBlank()) {
            throw AuthError.InvalidCredentials()
        }

        val mockSession = UserSession(
            userId = "user_debug_123",
            email = email,
            token = "debug"
        )

        saveSessionToStorage(mockSession)
        sessionState.value = mockSession
    }

    override suspend fun logout(): Result<Unit, AppException> = asResult(
        onError = {
            AppLogger.get()?.e(TAG, "Logout failed", it)
            it as? AppException ?: CommonError.UnknownError(it)
        }
    ) {
        storage.remove(KEY_USER_ID)
        storage.remove(KEY_TOKEN)
        storage.remove(KEY_EMAIL)

        sessionState.value = null
    }

    // --- Méthodes privées ---

    private fun loadSessionFromStorage(): UserSession? {
        val userId = storage.getString(KEY_USER_ID) ?: return null
        val token = storage.getString(KEY_TOKEN) ?: return null
        val email = storage.getString(KEY_EMAIL) ?: return null

        return UserSession(userId = userId, email = email, token = token)
    }

    private fun saveSessionToStorage(session: UserSession) {
        storage.putString(KEY_USER_ID, session.userId)
        storage.putString(KEY_TOKEN, session.token)
        storage.putString(KEY_EMAIL, session.email)
    }
}