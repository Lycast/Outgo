package fr.abknative.outgo.login.api

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

abstract class LoginPresenter : ViewModel() {
    abstract val state: StateFlow<LoginState>
    abstract val events: Flow<LoginEvent>
    abstract fun onIntent(intent: LoginIntent)
}