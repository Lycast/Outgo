package fr.abknative.outgo.login.api

/**
 * Represents user actions triggered from the Login UI.
 */
sealed interface LoginIntent {

    // --- User Inputs ---
    /** Triggered when the user types in the email field. */
    data class UpdateEmail(val email: String) : LoginIntent
    /** Triggered when the user types in the password field. */
    data class UpdatePassword(val password: String) : LoginIntent

    data class LoginWithGoogle(val idToken: String) : LoginIntent
    data class LoginWithApple(val idToken: String) : LoginIntent

    // --- Actions ---
    /** Submits the login request using the current state values. */
    object SubmitLogin : LoginIntent
    /** Submits the register request using the current state values. */
    object SubmitRegister : LoginIntent
    object Logout : LoginIntent
    object DismissError : LoginIntent
    object ResolveConflict : LoginIntent
    object CancelConflict : LoginIntent
    object RetrySync : LoginIntent
}