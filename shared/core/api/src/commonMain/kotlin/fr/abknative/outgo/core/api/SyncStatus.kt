package fr.abknative.outgo.core.api

enum class SyncStatus {
    SYNCED,
    PENDING_CREATE,
    PENDING_UPDATE,
    PENDING_DELETE,
    UNKNOWN;

    companion object {
        /**
         * Parse la chaîne de caractères de manière sécurisée.
         * Le fallback sur [UNKNOWN] empêche toute action de synchronisation accidentelle
         * si une valeur non reconnue est lue depuis la base de données locale ou le serveur.
         */
        fun fromString(value: String?): SyncStatus {
            return entries.find { it.name.equals(value, ignoreCase = true) } ?: UNKNOWN
        }
    }
}