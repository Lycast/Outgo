package fr.abknative.outgo.server.api

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.slf4j.LoggerFactory
import java.io.File

/**
 * Singleton object responsible for Firebase Admin SDK operations.
 * Handles initialization and token verification for secure backend routes.
 */
object FirebaseAdmin {

    private val logger = LoggerFactory.getLogger("FirebaseAdmin")

    /**
     * Initializes the Firebase Admin SDK.
     * Requires the FIREBASE_ADMIN_PATH environment variable to be present.
     * * @throws IllegalStateException if the environment variable is missing.
     */
    fun init() {
        val adminPath = System.getenv("FIREBASE_ADMIN_PATH")
            ?: error("FIREBASE_ADMIN_PATH is missing in environment")

        try {
            val file = File(adminPath)
            require(file.exists()) { "Firebase Admin JSON file not found at: $adminPath" }

            file.inputStream().use { stream ->
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
     * * @param token The ID token string sent from the client.
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