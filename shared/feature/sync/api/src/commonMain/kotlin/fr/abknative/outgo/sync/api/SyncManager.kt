package fr.abknative.outgo.sync.api

import fr.abknative.outgo.core.api.AppException
import fr.abknative.outgo.core.api.Result

/**
 * Interface principale pour la synchronisation des données.
 * C'est ce contrat que le WorkManager (Android) ou les Backgrounds Tasks (iOS) appelleront.
 */
interface SyncManager {

    /**
     * Exécute une synchronisation complète : Envoie les données locales (Push)
     * puis récupère les nouveautés du serveur (Pull).
     */
    suspend fun syncAll(): Result<Unit, AppException>

    /**
     * Envoie uniquement les modifications locales vers le serveur (Push).
     */
    suspend fun syncOut(): Result<Unit, AppException>

    /**
     * Récupère uniquement les nouveautés du serveur (Pull).
     */
    suspend fun syncIn(): Result<Unit, AppException>
}