package fr.abknative.outgo.subscription.impl

import fr.abknative.outgo.core.api.KeyValueStorage
import fr.abknative.outgo.core.api.TimeProvider
import fr.abknative.outgo.subscription.api.FeatureManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

internal class FeatureManagerImpl(
    private val storage: KeyValueStorage,
    private val timeProvider: TimeProvider
) : FeatureManager {

    companion object {
        private const val KEY_PREMIUM_UNTIL = "premium_until_timestamp"
    }

    private val _isPremiumFlow = MutableStateFlow(checkPremiumInternal())
    override val isPremiumFlow: StateFlow<Boolean> = _isPremiumFlow.asStateFlow()

    override fun isPremium(): Boolean = checkPremiumInternal()

    override fun updatePremiumStatus(untilTimestamp: Long) {
        storage.putLong(KEY_PREMIUM_UNTIL, untilTimestamp)
        _isPremiumFlow.value = checkPremiumInternal()
    }

    private fun checkPremiumInternal(): Boolean {
        val expireAt = storage.getLong(KEY_PREMIUM_UNTIL, 0L)
        return expireAt > timeProvider.now()
    }
}