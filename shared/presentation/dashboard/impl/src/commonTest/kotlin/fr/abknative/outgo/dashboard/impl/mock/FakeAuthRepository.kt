package fr.abknative.outgo.dashboard.impl.mock

import fr.abknative.outgo.auth.api.model.UserSession
import fr.abknative.outgo.auth.api.repository.AuthRepository
import fr.abknative.outgo.core.api.logs.AppException
import fr.abknative.outgo.core.api.logs.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FakeAuthRepository : AuthRepository {
    private val _sessionFlow = MutableStateFlow<UserSession?>(null)

    fun emit(session: UserSession?) {
        _sessionFlow.value = session
    }

    override fun observeSession(): StateFlow<UserSession?> = _sessionFlow

    override suspend fun getSession(): UserSession? = _sessionFlow.value

    override suspend fun register(email: String, password: String): Result<Unit, AppException> {
        return Result.Success(Unit)
    }

    override suspend fun login(email: String, password: String): Result<Unit, AppException> {
        return Result.Success(Unit)
    }

    override suspend fun logout(): Result<Unit, AppException> {
        _sessionFlow.value = null
        return Result.Success(Unit)
    }

    override suspend fun deleteAccount(): Result<Unit, AppException> {
        _sessionFlow.value = null
        return Result.Success(Unit)
    }
}