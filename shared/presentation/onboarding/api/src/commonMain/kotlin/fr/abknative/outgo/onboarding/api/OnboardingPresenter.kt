package fr.abknative.outgo.onboarding.api

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.StateFlow

abstract class OnboardingPresenter : ViewModel() {
    abstract val state: StateFlow<OnboardingState>
    abstract fun onIntent(intent: OnboardingIntent)
}