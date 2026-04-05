package fr.abknative.outgo.auth.impl.repository

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.FirebaseAuthException
import dev.gitlive.firebase.auth.FirebaseAuthInvalidCredentialsException
import dev.gitlive.firebase.auth.FirebaseAuthInvalidUserException
import dev.gitlive.firebase.auth.auth
import fr.abknative.outgo.auth.api.AuthError
import fr.abknative.outgo.auth.api.model.UserSession
import fr.abknative.outgo.auth.api.repository.AuthRepository
import fr.abknative.outgo.core.api.KeyValueStorage
import fr.abknative.outgo.core.api.logs.*
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Implementation of [AuthRepository] using Firebase Authentication.
 * Handles login, session management, and ensures token freshness for network calls.
 */
internal class AuthRepositoryImpl(
    private val storage: KeyValueStorage
) : AuthRepository {

    companion object {
        private const val KEY_USER_ID = "auth_user_id"
        private const val KEY_TOKEN = "auth_token"
        private const val KEY_EMAIL = "auth_email"
        private const val TAG = "FirebaseAuthRepo"
    }

    private val firebaseAuth = Firebase.auth
    private val sessionState = MutableStateFlow(loadSessionFromStorage())

    override fun observeSession(): Flow<UserSession?> = sessionState.asStateFlow()

    init {
        @OptIn(DelicateCoroutinesApi::class)
        GlobalScope.launch {
            firebaseAuth.authStateChanged.collect { firebaseUser ->
                if (firebaseUser == null) { clearLocalSession() }
            }
        }
    }

    /**
     * Retrieves the current user session.
     * Attempts to fetch a fresh JWT from Firebase to ensure backend requests
     * do not fail with 401 Unauthorized due to a locally expired token.
     */
    override suspend fun getSession(): UserSession? {
        val currentUser = firebaseAuth.currentUser

        if (currentUser != null) {
            try {
                val freshToken = currentUser.getIdToken(forceRefresh = false)

                if (freshToken != null) {
                    val updatedSession = UserSession(
                        userId = currentUser.uid,
                        email = currentUser.email ?: "",
                        token = freshToken
                    )

                    if (sessionState.value?.token != freshToken) {
                        saveSessionToStorage(updatedSession)
                        sessionState.value = updatedSession
                    }
                }
            } catch (e: Exception) {
                AppLogger.get()?.w(TAG, "Failed to refresh Firebase token, falling back to local storage", e)
            }
        }

        return sessionState.value
    }

    override suspend fun register(email: String, password: String): Result<Unit, AppException> = asResult(
        onError = ::mapFirebaseError
    ) {
        if (email.isBlank() || password.isBlank()) {
            throw AuthError.InvalidCredentials()
        }

        val authResult = firebaseAuth.createUserWithEmailAndPassword(email, password)
        val user = authResult.user ?: throw AuthError.UserNotFound()

        val token = user.getIdToken(forceRefresh = true) ?: throw CommonError.Unauthorized()

        val session = UserSession(
            userId = user.uid,
            email = user.email ?: email,
            token = token
        )

        saveSessionToStorage(session)
        sessionState.value = session
    }

    override suspend fun login(email: String, password: String): Result<Unit, AppException> = asResult(
        onError = ::mapFirebaseError
    ) {
        if (email.isBlank() || password.isBlank()) {
            throw AuthError.InvalidCredentials()
        }

        val authResult = firebaseAuth.signInWithEmailAndPassword(email, password)
        val user = authResult.user ?: throw AuthError.UserNotFound()

        val token = user.getIdToken(forceRefresh = true) ?: throw CommonError.Unauthorized()

        val session = UserSession(
            userId = user.uid,
            email = user.email ?: email,
            token = token
        )

        saveSessionToStorage(session)
        sessionState.value = session
    }

    override suspend fun logout(): Result<Unit, AppException> = asResult(
        onError = ::mapFirebaseError
    ) {
        firebaseAuth.signOut()
        clearLocalSession()
    }

    override suspend fun deleteAccount(): Result<Unit, AppException> = asResult(
        onError = ::mapFirebaseError
    ) {
        val user = firebaseAuth.currentUser ?: throw AuthError.UserNotFound()
        user.delete()
        clearLocalSession()
    }

    // --- Private Helpers ---

    /**
     * Maps Firebase-specific exceptions to our domain-specific [AppException].
     */
    private fun mapFirebaseError(e: Exception): AppException {
        AppLogger.get()?.e(TAG, "Firebase operation failed", e)
        return when (e) {
            is FirebaseAuthInvalidCredentialsException -> AuthError.InvalidCredentials()
            is FirebaseAuthInvalidUserException -> AuthError.UserNotFound()
            is FirebaseAuthException -> CommonError.Unauthorized(e)
            is AppException -> e
            else -> CommonError.UnknownError(e)
        }
    }

    private fun clearLocalSession() {
        storage.remove(KEY_USER_ID)
        storage.remove(KEY_TOKEN)
        storage.remove(KEY_EMAIL)
        sessionState.value = null
    }

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