package fr.abknative.outgo.dashboard.api

enum class SyncUiState {
    UNAUTHENTICATED, // L'utilisateur est en mode visiteur (Remplace OFFLINE, car offline = pas de wifi normalement)
    IN_PROGRESS,     // Le nuage tourne (Remplace SYNCING)
    UP_TO_DATE,      // Tout est aligné avec le serveur (Remplace SYNCED)
    ERROR;           // Le nuage est rouge/barré (Remplace NETWORK_ERROR)

    val isUnauthenticated: Boolean get() = this == UNAUTHENTICATED
    val isInProgress: Boolean get() = this == IN_PROGRESS
    val isUpToDate: Boolean get() = this == UP_TO_DATE
    val isError: Boolean get() = this == ERROR
}