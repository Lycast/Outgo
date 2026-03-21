package fr.abknative.outgo.core.api

import kotlinx.coroutines.CoroutineDispatcher

/**
 * Abstraction layer for coroutine dispatchers.
 * This ensures that dispatchers can be easily replaced during unit testing
 * (e.g., swapping [io] for a TestDispatcher) while maintaining consistent threading
 * policies across the application.
 */
interface AppDispatchers {

    /** Used for UI updates and lightweight, non-blocking operations. */
    val main: CoroutineDispatcher

    /** Used for heavy I/O operations such as network requests or database transactions. */
    val io: CoroutineDispatcher

    /** Used for CPU-intensive tasks, like sorting large lists or complex mathematical calculations. */
    val default: CoroutineDispatcher

    /** A dispatcher that is not confined to any specific thread. Use with caution. */
    val unconfined: CoroutineDispatcher
}