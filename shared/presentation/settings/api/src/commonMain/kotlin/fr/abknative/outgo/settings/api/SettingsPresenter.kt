package fr.abknative.outgo.settings.api

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.StateFlow

abstract class SettingsPresenter : ViewModel() {
    abstract val state: StateFlow<SettingsState>
    abstract fun onIntent(intent: SettingsIntent)
}