package fr.abknative.outgo.auth.api.repository

import fr.abknative.outgo.auth.api.model.UserSession
import fr.abknative.outgo.core.api.logs.AppException
import fr.abknative.outgo.core.api.logs.Result
import kotlinx.coroutines.flow.StateFlow

/**
 * Single source of truth for authentication and session management.
 * Handles credential verification, identity provider integrations (Google, Apple),
 * and maintains the current session state.
 */
interface AuthRepository {
    /**
     * Provides a reactive stream of the current user session.
     * Emits [UserSession] if authenticated, or null if logged out.
     */
    fun observeSession(): StateFlow<UserSession?>

    /**
     * Retrieves the current user session synchronously.
     * Useful for one-shot checks or interceptors (like HTTP clients) to retrieve a fresh token.
     */
    suspend fun getSession(): UserSession?

    /**
     * Creates a new user account using an email and password.
     */
    suspend fun register(email: String, password: String): Result<Unit, AppException>

    /**
     * Authenticates an existing user using their email and password.
     */
    suspend fun login(email: String, password: String): Result<Unit, AppException>

    /**
     * Triggers the sending of a verification email to the currently authenticated user.
     */
    suspend fun sendEmailVerification(): Result<Unit, AppException>

    /**
     * Forces a refresh of the user's state from the authentication provider's servers
     * and checks if their email address has been verified.
     *
     * @return A [Result] containing true if the email is verified, false otherwise.
     */
    suspend fun checkEmailVerified(): Result<Boolean, AppException>

    /**
     * Authenticates a user via Google Sign-In using the provided [idToken].
     */
    suspend fun loginWithGoogle(idToken: String): Result<Unit, AppException>

    /**
     * Authenticates a user via Apple Sign-In using the provided [idToken].
     */
    suspend fun loginWithApple(idToken: String): Result<Unit, AppException>

    /**
     * Signs out the current user and clears the local session data.
     */
    suspend fun logout(): Result<Unit, AppException>

    /**
     * Permanently deletes the current user's account from the authentication provider.
     */
    suspend fun deleteAccount(): Result<Unit, AppException>
}