package fr.abknative.outgo.onboarding.api

sealed interface OnboardingIntent {
    data class UpdateWalletName(val name: String) : OnboardingIntent
    data class UpdateIncomeAmount(val amount: String) : OnboardingIntent

    /** Déclenche la création séquentielle du Wallet puis du revenu. */
    object Submit : OnboardingIntent

    object DismissError : OnboardingIntent
}