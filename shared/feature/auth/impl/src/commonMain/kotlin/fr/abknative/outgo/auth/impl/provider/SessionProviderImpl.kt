package fr.abknative.outgo.auth.impl.provider

import fr.abknative.outgo.auth.api.provider.SessionProvider
import fr.abknative.outgo.core.api.KeyValueStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
internal class SessionProviderImpl(
    private val storage: KeyValueStorage
) : SessionProvider {

    companion object {
        private const val KEY_PERSISTENT_USER_ID = "persistent_user_id"
        private const val KEY_LAST_LOCAL_ID = "last_local_id"
    }

    private val _currentIdFlow = MutableStateFlow(loadOrGenerateId())

    override fun getCurrentUserId(): String {
        return _currentIdFlow.value
    }

    override fun observeUserId(): Flow<String> {
        return _currentIdFlow.asStateFlow()
    }

    override fun commitPersistentId(userId: String) {
        val currentId = _currentIdFlow.value
        if (userId != currentId && currentId.startsWith("local_")) {
            storage.putString(KEY_LAST_LOCAL_ID, currentId)
        }

        storage.putString(KEY_PERSISTENT_USER_ID, userId)
        _currentIdFlow.value = userId
    }

    override fun getLastLocalId(): String? {
        return storage.getString(KEY_LAST_LOCAL_ID)
    }

    /**
     * Reverts to the previous local ID if it exists.
     */
    fun revertToLastLocalId() {
        val lastLocal = storage.getString(KEY_LAST_LOCAL_ID)
        if (lastLocal != null) {
            commitPersistentId(lastLocal)
        } else {
            commitPersistentId("local_${Uuid.random()}")
        }
    }

    private fun loadOrGenerateId(): String {
        var lastId = storage.getString(KEY_PERSISTENT_USER_ID)
        if (lastId == null) {
            lastId = "local_${Uuid.random()}"
            storage.putString(KEY_PERSISTENT_USER_ID, lastId)
        }
        return lastId
    }
}