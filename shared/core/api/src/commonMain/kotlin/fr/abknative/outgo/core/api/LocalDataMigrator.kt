package fr.abknative.outgo.core.api

import fr.abknative.outgo.core.api.logs.AppException
import fr.abknative.outgo.core.api.logs.Result

interface LocalDataMigrator {
    /**
     * Vérifie si le téléphone contient des données appartenant à un autre compte.
     * Si les données appartiennent à l'ID Hors-ligne, elles sont migrées vers [newUserId].
     * * @param newUserId Le vrai UID Firebase qui vient de se connecter.
     * @param currentLocalId L'ID actuellement utilisé par le téléphone (Offline ID ou Ancien UID).
     * @return Success si tout va bien, Error(DataConflict) si conflit.
     */
    suspend fun checkConflictAndMigrate(newUserId: String, currentLocalId: String): Result<Unit, AppException>
}