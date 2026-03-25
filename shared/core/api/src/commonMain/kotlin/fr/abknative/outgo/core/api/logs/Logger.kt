package fr.abknative.outgo.core.api.logs

/**
 * Niveaux de sévérité standards pour la journalisation.
 */
enum class LogLevel {
    DEBUG, // Informations détaillées pour le développement local
    INFO,  // Événements normaux du cycle de vie (ex: "Synchro terminée")
    WARN,  // Situations anormales mais non bloquantes
    ERROR  // Défaillances techniques, exceptions
}

/**
 * Abstraction du système de journalisation.
 * Permet de router les logs vers différentes destinations (Console locale, Crashlytics, etc.)
 * sans coupler le code métier à une librairie spécifique.
 */
interface Logger {
    /**
     * Enregistre un message technique.
     *
     * @param level La sévérité du log.
     * @param tag Une étiquette pour filtrer facilement (ex: "SyncManager", "Auth").
     * @param message Le message technique.
     * @param throwable Une exception optionnelle à associer au log.
     */
    fun log(level: LogLevel, tag: String, message: String, throwable: Throwable? = null)

    // Fonctions de commodité pour alléger la syntaxe
    fun d(tag: String, message: String) = log(LogLevel.DEBUG, tag, message)
    fun i(tag: String, message: String) = log(LogLevel.INFO, tag, message)
    fun w(tag: String, message: String, throwable: Throwable? = null) = log(LogLevel.WARN, tag, message, throwable)
    fun e(tag: String, message: String, throwable: Throwable? = null) = log(LogLevel.ERROR, tag, message, throwable)
}

/**
 * Point d'accès statique pour les fonctions globales comme safeLaunch ou asResult.
 * Sera initialisé au démarrage de l'application.
 */
object AppLogger {
    private var internalLogger: Logger? = null

    fun initialize(logger: Logger) {
        internalLogger = logger
    }

    fun get(): Logger? = internalLogger
}