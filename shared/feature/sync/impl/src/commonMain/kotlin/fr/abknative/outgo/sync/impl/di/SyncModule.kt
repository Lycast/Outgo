package fr.abknative.outgo.sync.impl.di

import fr.abknative.outgo.core.api.DataPurger
import fr.abknative.outgo.sync.api.SyncManager
import fr.abknative.outgo.sync.api.SyncNetworkApi
import fr.abknative.outgo.sync.impl.SyncDataPurger
import fr.abknative.outgo.sync.impl.SyncManagerImpl
import fr.abknative.outgo.sync.impl.network.SyncNetworkApiImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun syncModule() = module {

    singleOf(::SyncNetworkApiImpl) { bind<SyncNetworkApi>() }
    singleOf(::SyncManagerImpl) { bind<SyncManager>() }

    singleOf(::SyncDataPurger) { bind<DataPurger>() }
}