package fr.abknative.outgo.auth.impl.usecase

import fr.abknative.outgo.auth.api.provider.SessionProvider
import fr.abknative.outgo.auth.api.repository.AuthRepository
import fr.abknative.outgo.auth.api.usecase.DeleteAccountUseCase
import fr.abknative.outgo.core.api.DataPurger
import fr.abknative.outgo.core.api.LocalDataDowngrader
import fr.abknative.outgo.core.api.TimeProvider
import fr.abknative.outgo.core.api.logs.AppException
import fr.abknative.outgo.core.api.logs.CommonError
import fr.abknative.outgo.core.api.logs.Result
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
internal class DeleteAccountUseCaseImpl(
    private val authRepository: AuthRepository,
    private val sessionProvider: SessionProvider,
    private val timeProvider: TimeProvider,
    private val httpClient: HttpClient,
    private val localDataPurgers: List<DataPurger>,
    private val downgraders: List<LocalDataDowngrader>
) : DeleteAccountUseCase {

    override suspend fun invoke(
        wipeLocal: Boolean,
        wipeServer: Boolean,
        revokeAuth: Boolean
    ): Result<Unit, AppException> {

        // Revoking auth inherently requires wiping the server data to avoid orphaned user data
        val shouldWipeServer = wipeServer || revokeAuth

        val currentUserId = sessionProvider.getCurrentUserId()
        val targetLocalId = sessionProvider.getLastLocalId() ?: "local_${Uuid.random()}"

        // 1. Handle Remote Data Deletion
        if (shouldWipeServer) {
            try {
                val response = httpClient.delete("/user/me")
                if (!response.status.isSuccess()) {
                    return Result.Error(CommonError.UnknownError(Exception("Failed to delete server data: ${response.status}")))
                }

                // If remote deletion succeeded and the user wants to KEEP local data,
                // we must update the local database to reflect the new state.
                if (!wipeLocal) {
                    val now = timeProvider.now()

                    if (revokeAuth) {
                        // Case B: Account destroyed. Detach data from Firebase UID and assign the new local ID.
                        downgraders.forEach {
                            it.downgradeToLocal(firebaseId = currentUserId, newLocalId = targetLocalId, now = now)
                        }
                    } else {
                        // Case A: Cloud wiped, but account remains. Queue existing data to be re-uploaded.
                        downgraders.forEach {
                            it.resetSyncStatusToPending(userId = currentUserId, now = now)
                        }
                    }
                }
            } catch (e: Exception) {
                return Result.Error(CommonError.NetworkError(e))
            }
        }

        // 2. Handle Authentication State
        if (revokeAuth) {
            val authResult = authRepository.deleteAccount()
            if (authResult is Result.Error) {
                return authResult
            }
        } else if (wipeLocal) {
            // Safety measure: If the user wipes their local app but keeps their cloud account,
            // we log them out to prevent accidental sync operations on a locally blank state.
            authRepository.logout()
        }

        // 3. Handle Local State & Sticky Identity
        if (wipeLocal) {
            try {
                localDataPurgers.forEach { it.purgeData(userId = currentUserId) }
                sessionProvider.commitPersistentId("")
            } catch (e: Exception) {
                return Result.Error(CommonError.DatabaseError(e))
            }
        } else if (revokeAuth) {
            sessionProvider.commitPersistentId(targetLocalId)
        }

        return Result.Success(Unit)
    }
}