package fr.abknative.outgo.core.api

import kotlinx.coroutines.CancellationException

/**
 * A type-safe wrapper representing either a successful [Success] or a failure [Error].
 *
 * This structure encourages explicit handling of both states, providing a robust
 * alternative to throwing exceptions across layers.
 */
sealed interface Result<out D, out E : AppException> {
    data class Success<out D>(val data: D) : Result<D, Nothing>
    data class Error<out E : AppException>(val error: E) : Result<Nothing, E>
}

/**
 * Executes [block] and wraps its result into a [Result].
 *
 * Re-throws [CancellationException] to preserve coroutine behavior
 * and maps other [Exception]s using [onError].
 */
inline fun <T> asResult(
    onError: (Exception) -> AppException = { CommonError.UnknownError(cause = it) },
    block: () -> T
): Result<T, AppException> {
    return try {
        Result.Success(block())
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        Result.Error(onError(e))
    }
}