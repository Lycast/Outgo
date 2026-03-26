package fr.abknative.outgo.auth.api.repository

import fr.abknative.outgo.auth.api.model.UserSession
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun observeSession(): Flow<UserSession?>
    suspend fun getSession(): UserSession?
    suspend fun login()
    suspend fun logout()
}