package fr.abknative.outgo.auth.impl

import fr.abknative.outgo.auth.api.repository.AuthRepository
import fr.abknative.outgo.core.api.DataPurger

internal class AuthDataPurger(
    private val authRepository: AuthRepository
) : DataPurger {
    override suspend fun purgeData() {
        authRepository.logout()
    }
}