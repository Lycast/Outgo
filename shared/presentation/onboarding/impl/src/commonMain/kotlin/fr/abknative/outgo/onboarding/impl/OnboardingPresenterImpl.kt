package fr.abknative.outgo.onboarding.impl

import androidx.lifecycle.viewModelScope
import fr.abknative.outgo.core.api.extensions.safeLaunch
import fr.abknative.outgo.core.api.logs.AppException
import fr.abknative.outgo.core.api.logs.Result
import fr.abknative.outgo.onboarding.api.OnboardingIntent
import fr.abknative.outgo.onboarding.api.OnboardingPresenter
import fr.abknative.outgo.onboarding.api.OnboardingState
import fr.abknative.outgo.wallet.api.usecase.InitializeBudgetUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

internal class OnboardingPresenterImpl(
    private val initializeBudget: InitializeBudgetUseCase
) : OnboardingPresenter() {

    private val _state = MutableStateFlow(OnboardingState())
    override val state: StateFlow<OnboardingState> = _state.asStateFlow()

    private val onCoroutineError: (AppException) -> Unit = { error ->
        _state.update { it.copy(isLoading = false, error = error) }
    }

    override fun onIntent(intent: OnboardingIntent) {
        when (intent) {
            is OnboardingIntent.UpdateWalletName -> _state.update { it.copy(walletName = intent.name) }
            is OnboardingIntent.UpdateIncomeAmount -> _state.update { it.copy(incomeAmountText = intent.amount) }
            is OnboardingIntent.DismissError -> _state.update { it.copy(error = null) }
            is OnboardingIntent.Submit -> handleSubmit()
        }
    }

    private fun handleSubmit() {
        if (_state.value.isLoading) return

        val currentName = _state.value.walletName.trim()
        val incomeAmountText = _state.value.incomeAmountText.trim()
        val incomeInCents = incomeAmountText.toLongOrNull()?.times(100) ?: 0L

        viewModelScope.safeLaunch(onError = onCoroutineError) {
            _state.update { it.copy(isLoading = true, error = null) }

            val result = initializeBudget(walletName = currentName, incomeInCents = incomeInCents)

            if (result is Result.Error) {
                _state.update { it.copy(isLoading = false, error = result.error) }
            } else {
                _state.update { it.copy(isLoading = false, isCompleted = true) }
            }
        }
    }
}