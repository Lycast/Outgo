package fr.abknative.outgo.android.ui.login.helper

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import kotlinx.coroutines.CancellationException

/**
 * Extracts the closest Activity from a given Context.
 *
 * @return The underlying Activity, or null if it cannot be found.
 */
fun Context.findActivity(): Activity? {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    return null
}

/**
 * Launches the Android Credential Manager to perform Google Sign-In.
 *
 * @param context The current Android context required to display the bottom sheet.
 * @param webClientId The Web Client ID from the Firebase console.
 * @return A [CredentialResult] representing success with the ID token, cancellation, or a localized error.
 */
suspend fun launchGoogleSignIn(context: Context, webClientId: String): CredentialResult {
    return try {
        val activityContext = context.findActivity() ?: context
        val credentialManager = CredentialManager.create(activityContext)

        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(webClientId)
            .setAutoSelectEnabled(true)
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        val result = credentialManager.getCredential(
            request = request,
            context = activityContext
        )

        val credential = result.credential
        if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
            val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
            CredentialResult.Success(googleIdTokenCredential.idToken)
        } else {
            CredentialResult.Error(CredentialErrorType.INVALID_TOKEN)
        }
    } catch (e: CancellationException) {
        throw e
    } catch (e: GetCredentialCancellationException) {
        Log.e("AuthDebug", "GetCredentialCancellationException: ", e)
        CredentialResult.Cancelled
    } catch (e: NoCredentialException) {
        Log.e("AuthDebug", "NoCredentialException: ", e)
        CredentialResult.Error(CredentialErrorType.NO_ACCOUNT_FOUND)
    } catch (e: GetCredentialException) {
        Log.e("AuthDebug", "Credential Exception: ", e)
        CredentialResult.Error(CredentialErrorType.SYSTEM_ERROR, e.message)
    } catch (e: Throwable) {
        Log.e("AuthDebug", "Unknown Throwable: ", e)
        CredentialResult.Error(CredentialErrorType.UNKNOWN)
    }
}