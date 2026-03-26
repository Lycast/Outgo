package fr.abknative.outgo.auth.api.repository

import fr.abknative.outgo.auth.api.model.UserSession
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun observeSession(): Flow<UserSession?>
    suspend fun getSession(): UserSession?
    suspend fun login(email: String, password: String)
    suspend fun logout()
}