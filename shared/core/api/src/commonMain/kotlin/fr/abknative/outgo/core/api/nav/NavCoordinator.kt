package fr.abknative.outgo.core.api.nav

import kotlinx.coroutines.flow.StateFlow

sealed interface AppStep {
    data object Splash : AppStep
    data object Onboarding : AppStep
    data object Dashboard : AppStep
    data object Analyse : AppStep
    data object Settings : AppStep
    data object Login : AppStep
}

data class NavigationState(
    val stack: List<AppStep> = listOf(AppStep.Splash)
) {
    val currentStep: AppStep get() = stack.last()
    val canGoBack: Boolean get() = stack.size > 1
}

interface NavCoordinator {
    val state: StateFlow<NavigationState>
    fun navigateTo(step: AppStep)
    fun replaceRoot(step: AppStep)
    fun handleBack(): Boolean
}
