package fr.abknative.outgo.subscription.impl

import fr.abknative.outgo.core.api.DataPurger
import fr.abknative.outgo.subscription.api.FeatureManager

internal class SubscriptionDataPurger(
    private val featureManager: FeatureManager
) : DataPurger {
    override suspend fun purgeData() {
        featureManager.updatePremiumStatus(0L)
    }
}