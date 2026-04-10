package fr.abknative.outgo.onboarding.api

import fr.abknative.outgo.core.api.logs.AppException

/**
 * Représente l'état de l'écran de bienvenue/configuration initiale.
 */
data class OnboardingState(
    val walletName: String = "",
    val incomeAmountText: String = "",
    val isLoading: Boolean = false,
    val error: AppException? = null,
    val isCompleted: Boolean = false
)