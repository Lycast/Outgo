package fr.abknative.outgo.outgoing.api.presenter

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.StateFlow


/**
 * Base contract for the Outgoing screen presentation logic, implementing a
 * Unidirectional Data Flow (UDF) pattern.
 *
 * **Architectural Note:** This class extends [ViewModel] as a strategic choice
 * to leverage a unified, lifecycle-aware primitive across platforms (Android, iOS, Web).
 * This approach ensures consistent **structured concurrency** and automatic memory management while maintaining a UI-agnostic contract.
 */
abstract class OutgoingPresenter : ViewModel() {
    /**
     * Observable stream of the current UI state.
     */
    abstract val state: StateFlow<OutgoingState>

    /**
     * Dispatches a user intention to the business logic.
     * @param intent The action representing the user's goal.
     */
    abstract fun onIntent(intent: OutgoingIntent)
}