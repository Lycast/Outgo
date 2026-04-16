package fr.abknative.outgo.core.impl.logs

import fr.abknative.outgo.core.api.logs.AppException
import fr.abknative.outgo.core.api.logs.CommonError
import fr.abknative.outgo.core.api.logs.ExceptionMapper
import io.ktor.client.network.sockets.*
import io.ktor.client.plugins.*
import io.ktor.http.*
import io.ktor.util.network.*

/**
 * Implémentation Ktor du pont [ExceptionMapper].
 */
class KtorExceptionMapper : ExceptionMapper {

    override fun map(exception: Exception): AppException {
        if (exception is AppException) return exception

        return when (exception) {
            is HttpRequestTimeoutException,
            is ConnectTimeoutException,
            is SocketTimeoutException -> CommonError.Timeout(exception)

            is UnresolvedAddressException -> CommonError.NetworkError(exception)

            is ClientRequestException -> {
                when (exception.response.status) {
                    HttpStatusCode.Unauthorized,
                    HttpStatusCode.Forbidden -> CommonError.Unauthorized(exception)
                    else -> CommonError.ServerError(exception)
                }
            }

            is ServerResponseException -> CommonError.ServerError(exception)

            else -> CommonError.UnknownError(exception)
        }
    }
}