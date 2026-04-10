package fr.abknative.outgo.auth.impl

import fr.abknative.outgo.auth.api.provider.SessionProvider
import fr.abknative.outgo.auth.api.repository.AuthRepository
import fr.abknative.outgo.core.api.DataPurger

internal class AuthDataPurger(
    private val authRepository: AuthRepository,
    private val sessionProvider: SessionProvider
) : DataPurger {
    override suspend fun purgeData(userId: String?) {
        val currentId = sessionProvider.getCurrentUserId()

        if (userId == null || userId == currentId) {
            authRepository.logout()
        }
    }
}