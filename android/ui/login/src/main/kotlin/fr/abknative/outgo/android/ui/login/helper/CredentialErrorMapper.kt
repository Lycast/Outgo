package fr.abknative.outgo.android.ui.login.helper

enum class CredentialErrorType {
    INVALID_TOKEN,
    NO_ACCOUNT_FOUND,
    SYSTEM_ERROR,
    UNKNOWN
}

sealed interface CredentialResult {

    data class Success(val idToken: String) : CredentialResult
    data object Cancelled : CredentialResult
    data class Error(val type: CredentialErrorType, val systemMessage: String? = null) : CredentialResult
}