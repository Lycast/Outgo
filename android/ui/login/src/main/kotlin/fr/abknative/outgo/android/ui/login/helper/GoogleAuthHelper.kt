package fr.abknative.outgo.android.ui.login.helper

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential

/**
 * Launches the Android Credential Manager to perform Google Sign-In.
 *
 * @param context The current Android context required to display the bottom sheet.
 * @param webClientId The Web Client ID from the Firebase console.
 * @return A [CredentialResult] representing success with the ID token, cancellation, or a localized error.
 */
suspend fun launchGoogleSignIn(context: Context, webClientId: String): CredentialResult {
    val credentialManager = CredentialManager.create(context)

    val googleIdOption = GetGoogleIdOption.Builder()
        .setFilterByAuthorizedAccounts(false)
        .setServerClientId(webClientId)
        .setAutoSelectEnabled(true)
        .build()

    val request = GetCredentialRequest.Builder()
        .addCredentialOption(googleIdOption)
        .build()

    return try {
        val result = credentialManager.getCredential(
            request = request,
            context = context
        )

        val credential = result.credential
        if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
            val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
            CredentialResult.Success(googleIdTokenCredential.idToken)
        } else {
            CredentialResult.Error(CredentialErrorType.INVALID_TOKEN)
        }
    } catch (e: GetCredentialCancellationException) {
        CredentialResult.Cancelled
    } catch (e: NoCredentialException) {
        CredentialResult.Error(CredentialErrorType.NO_ACCOUNT_FOUND)
    } catch (e: GetCredentialException) {
        CredentialResult.Error(CredentialErrorType.SYSTEM_ERROR, e.message)
    } catch (e: Exception) {
        CredentialResult.Error(CredentialErrorType.UNKNOWN)
    }
}