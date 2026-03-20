package fr.abknative.outgo.app.nav

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

sealed interface AppStep {
    data object Dashboard : AppStep
    data object Settings : AppStep
}

data class NavigationState(
    val stack: List<AppStep> = listOf(AppStep.Dashboard)
) {
    val currentStep: AppStep get() = stack.last()
    val canGoBack: Boolean get() = stack.size > 1
}

class AppCoordinator {
    private val _state = MutableStateFlow(NavigationState())
    val state = _state.asStateFlow()

    private val navigationMutex = Mutex()

    suspend fun navigateTo(step: AppStep) {
        navigationMutex.withLock {
            _state.update { currentState ->
                if (currentState.currentStep == step) return@update currentState
                currentState.copy(stack = currentState.stack + step)
            }
        }
    }

    fun handleBack(): Boolean {
        return if (_state.value.canGoBack) {
            _state.update { it.copy(stack = it.stack.dropLast(1)) }
            true
        } else {
            false
        }
    }
}