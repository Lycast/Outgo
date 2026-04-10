package fr.abknative.outgo.subscription.impl

import fr.abknative.outgo.auth.api.provider.SessionProvider
import fr.abknative.outgo.core.api.DataPurger
import fr.abknative.outgo.subscription.api.FeatureManager

internal class SubscriptionDataPurger(
    private val featureManager: FeatureManager,
    private val sessionProvider: SessionProvider
) : DataPurger {
    override suspend fun purgeData(userId: String?) {
        val currentId = sessionProvider.getCurrentUserId()

        if (userId == null || userId == currentId) {
            featureManager.updatePremiumStatus(0L)
        }
    }
}