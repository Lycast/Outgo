package fr.abknative.outgo.shell.api

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.StateFlow

abstract class ShellPresenter : ViewModel() {
    abstract val state: StateFlow<ShellState>
    abstract fun onIntent(intent: ShellIntent)
}