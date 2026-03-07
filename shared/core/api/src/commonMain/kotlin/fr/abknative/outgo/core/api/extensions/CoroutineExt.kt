package fr.abknative.outgo.core.api.extensions

import fr.abknative.outgo.core.api.AppException
import fr.abknative.outgo.core.api.CommonError
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Launches a coroutine that automatically catches unhandled exceptions.
 *
 * Any error occurring inside the [block] is caught, transformed into an [AppException],
 * and passed to the [onError] callback.
 *
 * @param onError Callback receiving the transformed [AppException].
 * @param block The suspendable code to execute.
 */
fun CoroutineScope.safeLaunch(
    onError: (AppException) -> Unit,
    block: suspend CoroutineScope.() -> Unit
) {
    val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        val appException = throwable as? AppException ?: CommonError.UnknownError(cause = throwable)
        onError(appException)
    }

    launch(exceptionHandler) {
        block()
    }
}