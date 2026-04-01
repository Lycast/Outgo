package fr.abknative.outgo.core.api

import kotlinx.coroutines.flow.StateFlow

interface NetworkMonitor {
    /**
     * Indique si l'appareil dispose d'une connexion internet active.
     */
    val isConnected: StateFlow<Boolean>
}