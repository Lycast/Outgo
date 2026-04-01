package fr.abknative.outgo.auth.api.repository

import fr.abknative.outgo.auth.api.model.UserSession
import fr.abknative.outgo.core.api.logs.AppException
import fr.abknative.outgo.core.api.logs.Result
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun observeSession(): Flow<UserSession?>
    suspend fun getSession(): UserSession?

    suspend fun login(email: String, password: String): Result<Unit, AppException>
    suspend fun logout(): Result<Unit, AppException>
    suspend fun deleteAccount(): Result<Unit, AppException>
}