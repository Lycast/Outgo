package fr.abknative.outgo.month.api

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.StateFlow

/**
 * Base contract for the Month screen presentation logic.
 */
abstract class MonthPresenter : ViewModel() {
    /**
     * Observable stream of the current UI state.
     */
    abstract val state: StateFlow<MonthState>

    /**
     * Dispatches a user intention to the business logic.
     * @param intent The action representing the user's goal.
     */
    abstract fun onIntent(intent: MonthIntent)
}