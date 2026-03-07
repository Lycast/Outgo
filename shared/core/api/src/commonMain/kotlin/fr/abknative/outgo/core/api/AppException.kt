package fr.abknative.outgo.core.api

/**
 * Base class for all domain-specific exceptions.
 * * Ensures that every error in the application follows a unified structure
 * while preserving the original [cause] for debugging purposes.
 */
abstract class AppException(
    override val message: String? = null,
    override val cause: Throwable? = null
) : Throwable(message, cause)

/**
 * Common technical errors shared across the entire application.
 */
sealed class CommonError(message: String? = null, cause: Throwable? = null) : AppException(message, cause) {
    class NetworkError(cause: Throwable? = null) : CommonError("Network connection failed", cause)
    class DatabaseError(cause: Throwable? = null) : CommonError("Local database operation failed", cause)
    class UnknownError(cause: Throwable? = null) : CommonError("Unknown technical error", cause)
}
