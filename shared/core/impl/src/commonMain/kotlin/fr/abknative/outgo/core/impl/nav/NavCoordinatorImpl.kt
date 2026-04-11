package fr.abknative.outgo.core.impl.nav

import fr.abknative.outgo.core.api.nav.AppStep
import fr.abknative.outgo.core.api.nav.NavCoordinator
import fr.abknative.outgo.core.api.nav.NavigationState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

internal class NavCoordinatorImpl : NavCoordinator {
    private val _state = MutableStateFlow(NavigationState())
    override val state = _state.asStateFlow()

    override fun navigateTo(step: AppStep) {
        _state.update { currentState ->
            if (currentState.currentStep == step) return@update currentState
            currentState.copy(stack = currentState.stack + step)
        }
    }

    override fun replaceRoot(step: AppStep) {
        _state.update { it.copy(stack = listOf(step)) }
    }

    override fun handleBack(): Boolean {
        return if (_state.value.canGoBack) {
            _state.update { it.copy(stack = it.stack.dropLast(1)) }
            true
        } else {
            false
        }
    }
}