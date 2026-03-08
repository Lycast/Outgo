package fr.abknative.outgo.core.api.extensions

import fr.abknative.outgo.core.api.AppException
import fr.abknative.outgo.core.api.CommonError
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

/**
 * Launches a coroutine that automatically catches unhandled exceptions.
 *
 * Any error occurring inside the [block] is caught, transformed into an [AppException],
 * and passed to the [onError] callback.
 * * Note: Exceptions occurring inside Flow streams should be handled using the `catch` operator
 * on the Flow itself, rather than relying on this handler.
 *
 * @param context Additional to [CoroutineScope.coroutineContext] context of the coroutine.
 * @param onError Callback receiving the transformed [AppException].
 * @param block The suspendable code to execute.
 */
fun CoroutineScope.safeLaunch(
    context: CoroutineContext = EmptyCoroutineContext,
    onError: (AppException) -> Unit,
    block: suspend CoroutineScope.() -> Unit
) {
    val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        val appException = throwable as? AppException ?: CommonError.UnknownError(cause = throwable)
        onError(appException)
    }

    launch(context + exceptionHandler) {
        block()
    }
}