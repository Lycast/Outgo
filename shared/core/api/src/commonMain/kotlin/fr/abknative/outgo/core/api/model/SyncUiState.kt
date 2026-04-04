package fr.abknative.outgo.core.api.model

enum class SyncUiState {
    UNAUTHENTICATED, // 1. Cloud barré
    OFFLINE,         // 2. Pas de réseau (Cloud warning)
    PENDING,         // 3. Données locales en attente (Arrows statiques)
    IN_PROGRESS,     // 4. Synchronisation en cours (Arrows animées)
    UP_TO_DATE,      // 5. Tout est synchronisé (Cloud check)
    ERROR;           // Cas d'erreur serveur

    val isUnauthenticated: Boolean get() = this == UNAUTHENTICATED
    val isOffline: Boolean get() = this == OFFLINE
    val isPending: Boolean get() = this == PENDING
    val isInProgress: Boolean get() = this == IN_PROGRESS
    val isUpToDate: Boolean get() = this == UP_TO_DATE
    val isError: Boolean get() = this == ERROR
}