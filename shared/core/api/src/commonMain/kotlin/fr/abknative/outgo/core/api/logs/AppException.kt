package fr.abknative.outgo.core.api.logs

/**
 * Base class for all domain-specific exceptions.
 * * Ensures that every error in the application follows a unified structure
 * while preserving the original [cause] for debugging purposes.
 */
abstract class AppException(
    override val cause: Throwable? = null
) : Throwable(null, cause)

/**
 * Common technical errors shared across the entire application.
 */
sealed class CommonError(cause: Throwable? = null) : AppException(cause) {
    class NetworkError(cause: Throwable? = null) : CommonError(cause)
    class DatabaseError(cause: Throwable? = null) : CommonError(cause)
    class UnknownError(cause: Throwable? = null) : CommonError(cause)
}
