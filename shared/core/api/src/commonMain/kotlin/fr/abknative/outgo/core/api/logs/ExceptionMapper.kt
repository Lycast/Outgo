package fr.abknative.outgo.core.api.logs

/**
 * Contrat (Pont) permettant de traduire une exception technique
 * en une erreur métier [AppException], sans exposer la technologie sous-jacente.
 */
interface ExceptionMapper {
    fun map(exception: Exception): AppException
}