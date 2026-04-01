package fr.abknative.outgo.core.impl.di

import fr.abknative.outgo.core.api.KeyValueStorage
import fr.abknative.outgo.core.api.NetworkMonitor
import fr.abknative.outgo.core.impl.IosKeyValueStorage
import fr.abknative.outgo.core.impl.IosNetworkMonitor
import io.ktor.client.engine.*
import io.ktor.client.engine.darwin.*
import org.koin.dsl.module

actual val platformCoreModule = module {

    single<KeyValueStorage> { IosKeyValueStorage() }
    single<HttpClientEngine> { Darwin.create() }
    single<NetworkMonitor> { IosNetworkMonitor() }
}