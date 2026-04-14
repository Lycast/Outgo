package fr.abknative.outgo.operation.api

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.StateFlow

/**
 * Base contract for the Operation Form presentation logic.
 */
abstract class OperationPresenter : ViewModel() {
    abstract val state: StateFlow<OperationState>
    abstract fun onIntent(intent: OperationIntent)
}