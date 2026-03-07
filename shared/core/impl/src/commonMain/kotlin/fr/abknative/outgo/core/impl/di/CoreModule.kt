package fr.abknative.outgo.core.impl.di

import fr.abknative.outgo.core.api.AppDispatchers
import fr.abknative.outgo.core.api.TimeProvider
import fr.abknative.outgo.core.impl.RealTimeProvider
import fr.abknative.outgo.core.impl.StandardDispatchers
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun coreModule() = module {
    singleOf(::RealTimeProvider) { bind<TimeProvider>() }
    singleOf(::StandardDispatchers) { bind<AppDispatchers>() }
}