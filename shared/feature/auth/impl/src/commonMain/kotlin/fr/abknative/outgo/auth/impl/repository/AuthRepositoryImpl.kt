package fr.abknative.outgo.auth.impl.repository

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.FirebaseUser
import dev.gitlive.firebase.auth.GoogleAuthProvider
import dev.gitlive.firebase.auth.OAuthProvider
import dev.gitlive.firebase.auth.auth
import fr.abknative.outgo.auth.api.AuthError
import fr.abknative.outgo.auth.api.model.UserSession
import fr.abknative.outgo.auth.api.repository.AuthRepository
import fr.abknative.outgo.auth.impl.toAuthAppException
import fr.abknative.outgo.core.api.AppDispatchers
import fr.abknative.outgo.core.api.KeyValueStorage
import fr.abknative.outgo.core.api.logs.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Implementation of [AuthRepository] using Firebase Authentication.
 * Handles login, session management, and ensures token freshness for network calls.
 */
internal class AuthRepositoryImpl(
    private val storage: KeyValueStorage,
    private val dispatchers: AppDispatchers
) : AuthRepository {

    companion object {
        private const val KEY_USER_ID = "auth_user_id"
        private const val KEY_TOKEN = "auth_token"
        private const val KEY_EMAIL = "auth_email"
        private const val KEY_EMAIL_VERIFIED = "auth_email_verified"
        private const val TAG = "FirebaseAuthRepo"
    }

    private val firebaseAuth = Firebase.auth
    private val sessionState = MutableStateFlow(loadSessionFromStorage())
    private val repoScope = CoroutineScope(SupervisorJob() + dispatchers.io)

    override fun observeSession(): StateFlow<UserSession?> = sessionState.asStateFlow()

    init {
        repoScope.launch {
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
                        token = freshToken,
                        isEmailVerified = currentUser.isEmailVerified
                    )

                    val currentSession = sessionState.value
                    if (currentSession?.token != freshToken || currentSession.isEmailVerified != currentUser.isEmailVerified) {
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
        onError = { it.toAuthAppException(TAG) }
    ) {
        if (email.isBlank() || password.isBlank()) throw AuthError.InvalidCredentials()

        val authResult = firebaseAuth.createUserWithEmailAndPassword(email, password)
        createAndSaveSession(authResult.user, email)
    }

    override suspend fun login(email: String, password: String): Result<Unit, AppException> = asResult(
        onError = { it.toAuthAppException(TAG) }
    ) {
        if (email.isBlank() || password.isBlank()) throw AuthError.InvalidCredentials()

        val authResult = firebaseAuth.signInWithEmailAndPassword(email, password)
        createAndSaveSession(authResult.user, email)
    }

    override suspend fun sendEmailVerification(): Result<Unit, AppException> = asResult(
        onError = { it.toAuthAppException(TAG) }
    ) {
        val user = firebaseAuth.currentUser ?: throw AuthError.UserNotFound()
        user.sendEmailVerification()
    }

    override suspend fun checkEmailVerified(): Result<Boolean, AppException> = asResult(
        onError = { it.toAuthAppException(TAG) }
    ) {
        val user = firebaseAuth.currentUser ?: return@asResult false

        user.reload()
        val isVerified = user.isEmailVerified

        val currentSession = sessionState.value
        if (currentSession != null && currentSession.isEmailVerified != isVerified) {
            val updatedSession = currentSession.copy(isEmailVerified = isVerified)
            saveSessionToStorage(updatedSession)
            sessionState.value = updatedSession
        }

        isVerified
    }

    override suspend fun loginWithGoogle(idToken: String): Result<Unit, AppException> = asResult(
        onError = { it.toAuthAppException(TAG) }
    ) {
        val credential = GoogleAuthProvider.credential(idToken = idToken, accessToken = null)
        val authResult = firebaseAuth.signInWithCredential(credential)
        createAndSaveSession(authResult.user, authResult.user?.email ?: "")
    }

    override suspend fun loginWithApple(idToken: String): Result<Unit, AppException> = asResult(
        onError = { it.toAuthAppException(TAG) }
    ) {
        val credential = OAuthProvider.credential(
            providerId = "apple.com",
            idToken = idToken,
            accessToken = null
        )

        val authResult = firebaseAuth.signInWithCredential(credential)
        createAndSaveSession(authResult.user, authResult.user?.email ?: "")
    }

    override suspend fun logout(): Result<Unit, AppException> = asResult(
        onError = { it.toAuthAppException(TAG) }
    ) {
        firebaseAuth.signOut()
        clearLocalSession()
    }

    override suspend fun deleteAccount(): Result<Unit, AppException> = asResult(
        onError = { it.toAuthAppException(TAG) }
    ) {
        val user = firebaseAuth.currentUser ?: throw AuthError.UserNotFound()
        user.delete()
        clearLocalSession()
    }

    // --- Private Helpers ---

    /**
     * Extracts the token, creates the UserSession, and saves it locally.
     * Prevents code duplication between login and register.
     */
    private suspend fun createAndSaveSession(user: FirebaseUser?, fallbackEmail: String) {
        if (user == null) throw AuthError.UserNotFound()

        val token = user.getIdToken(forceRefresh = true) ?: throw CommonError.Unauthorized()

        val session = UserSession(
            userId = user.uid,
            email = user.email ?: fallbackEmail,
            token = token,
            isEmailVerified = user.isEmailVerified
        )

        saveSessionToStorage(session)
        sessionState.value = session
    }

    private fun clearLocalSession() {
        storage.remove(KEY_USER_ID)
        storage.remove(KEY_TOKEN)
        storage.remove(KEY_EMAIL)
        storage.remove(KEY_EMAIL_VERIFIED)
        sessionState.value = null
    }

    private fun loadSessionFromStorage(): UserSession? {
        val userId = storage.getString(KEY_USER_ID) ?: return null
        val token = storage.getString(KEY_TOKEN) ?: return null
        val email = storage.getString(KEY_EMAIL) ?: return null
        val isEmailVerified = storage.getBoolean(KEY_EMAIL_VERIFIED, false)

        return UserSession(
            userId = userId,
            email = email,
            token = token,
            isEmailVerified = isEmailVerified
        )
    }

    private fun saveSessionToStorage(session: UserSession) {
        storage.putString(KEY_USER_ID, session.userId)
        storage.putString(KEY_TOKEN, session.token)
        storage.putString(KEY_EMAIL, session.email)
        storage.putBoolean(KEY_EMAIL_VERIFIED, session.isEmailVerified)
    }
}