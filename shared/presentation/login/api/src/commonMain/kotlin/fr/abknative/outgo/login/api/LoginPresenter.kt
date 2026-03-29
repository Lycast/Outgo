package fr.abknative.outgo.login.api

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.StateFlow

abstract class LoginPresenter : ViewModel() {
    abstract val state: StateFlow<LoginState>
    abstract fun onIntent(intent: LoginIntent)
}