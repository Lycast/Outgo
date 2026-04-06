package fr.abknative.outgo.subscription.impl.di

import fr.abknative.outgo.core.api.DataPurger
import fr.abknative.outgo.subscription.api.FeatureManager
import fr.abknative.outgo.subscription.impl.FeatureManagerImpl
import fr.abknative.outgo.subscription.impl.SubscriptionDataPurger
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val subscriptionModule = module {
    singleOf(::FeatureManagerImpl) { bind<FeatureManager>() }
    singleOf(::SubscriptionDataPurger) { bind<DataPurger>() }
}