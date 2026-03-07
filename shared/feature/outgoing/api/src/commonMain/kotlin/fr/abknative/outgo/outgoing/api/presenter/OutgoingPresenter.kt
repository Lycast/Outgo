package fr.abknative.outgo.outgoing.api.presenter

import kotlinx.coroutines.flow.StateFlow

/**
 * Contract for the UI-agnostic presentation logic.
 *
 * Implements a Unidirectional Data Flow (UDF) where the [state] represents the
 * single source of truth and [onIntent] serves as the unique entry point for user actions.
 */
interface OutgoingPresenter {
    /**
     * Observable stream of the current UI state to be consumed by native views.
     */
    val state: StateFlow<OutgoingState>

    /**
    * Dispatches a user intention to be processed by the business logic.
    ** @param intent The action representing the user's goal.
    */
    fun onIntent(intent: OutgoingIntent)
}