package fr.abknative.outgo.android.ui.login.helper

enum class CredentialErrorType {
    INVALID_TOKEN,
    NO_ACCOUNT_FOUND,
    SYSTEM_ERROR,
    UNKNOWN
}

/**
 * Represents the result of a Google Sign-In authentication process.
 */
sealed interface CredentialResult {

    /**
     * Represents a successful authentication.
     *
     * @property idToken The ID token returned by the authentication provider.
     */
    data class Success(val idToken: String) : CredentialResult

    /**
     * Represents a cancellation of the authentication process by the user.
     */
    data object Cancelled : CredentialResult

    /**
     * Represents a failure during the authentication process.
     *
     * @property type The specific category of the error.
     * @property systemMessage An optional system-provided message for debugging purposes.
     */
    data class Error(val type: CredentialErrorType, val systemMessage: String? = null) : CredentialResult
}