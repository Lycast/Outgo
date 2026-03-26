package fr.abknative.outgo.auth.api.presenter

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.StateFlow

abstract class AuthPresenter : ViewModel() {
    abstract val state: StateFlow<AuthState>
    abstract fun onIntent(intent: AuthIntent)
}