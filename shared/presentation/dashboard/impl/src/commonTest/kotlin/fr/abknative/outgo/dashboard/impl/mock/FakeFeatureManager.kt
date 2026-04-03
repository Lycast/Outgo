package fr.abknative.outgo.dashboard.impl.mock

import fr.abknative.outgo.subscription.api.FeatureManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FakeFeatureManager(var isPremiumMock: Boolean = false) : FeatureManager {
    override val isPremiumFlow: StateFlow<Boolean> = MutableStateFlow(isPremiumMock)
    override fun isPremium(): Boolean = isPremiumMock
    override fun updatePremiumStatus(untilTimestamp: Long) { /* no-op */ }
}