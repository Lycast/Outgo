package fr.abknative.outgo.server.api

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.slf4j.LoggerFactory
import java.io.ByteArrayInputStream
import java.io.File
import java.io.InputStream
import java.util.*

/**
 * Singleton object responsible for Firebase Admin SDK operations.
 * Handles initialization and token verification for secure backend routes.
 */
object FirebaseAdmin {

    private val logger = LoggerFactory.getLogger("FirebaseAdmin")

    /**
     * Initializes the Firebase Admin SDK.
     * Evaluates environment variables in the following order:
     * 1. FIREBASE_ADMIN_BASE64: Used in production (Serverless environments).
     * 2. FIREBASE_ADMIN_PATH: Used in local development environments.
     *
     * @throws IllegalStateException if neither environment variable is configured properly.
     */
    fun init() {
        try {
            val base64Credentials = System.getenv("FIREBASE_ADMIN_BASE64")
            val adminPath = System.getenv("FIREBASE_ADMIN_PATH")

            val inputStream: InputStream = when {
                !base64Credentials.isNullOrBlank() -> {
                    logger.info("Using FIREBASE_ADMIN_BASE64 for Firebase initialization.")
                    val decodedBytes = Base64.getDecoder().decode(base64Credentials)
                    ByteArrayInputStream(decodedBytes)
                }
                !adminPath.isNullOrBlank() -> {
                    logger.info("Using FIREBASE_ADMIN_PATH for Firebase initialization.")
                    val file = File(adminPath)
                    require(file.exists()) { "Firebase Admin JSON file not found at: $adminPath" }
                    file.inputStream()
                }
                else -> error("Missing FIREBASE_ADMIN_BASE64 or FIREBASE_ADMIN_PATH in environment variables")
            }

            inputStream.use { stream ->
                val options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(stream))
                    .build()

                if (FirebaseApp.getApps().isEmpty()) {
                    FirebaseApp.initializeApp(options)
                    logger.info("Firebase Admin SDK initialized successfully.")
                }
            }
        } catch (e: Exception) {
            logger.error("Failed to initialize Firebase Admin SDK", e)
            throw e
        }
    }

    /**
     * Verifies a Firebase ID token asynchronously.
     *
     * @param token The ID token string sent from the client.
     * @return The decoded [FirebaseToken] if valid, null otherwise.
     */
    suspend fun verifyToken(token: String): FirebaseToken? = withContext(Dispatchers.IO) {
        try {
            FirebaseAuth.getInstance().verifyIdToken(token)
        } catch (e: Exception) {
            logger.warn("Firebase Auth token verification failed: ${e.message}")
            null
        }
    }
}