package fr.abknative.outgo.core.impl.di

import fr.abknative.outgo.core.api.KeyValueStorage
import fr.abknative.outgo.core.impl.AndroidKeyValueStorage
import io.ktor.client.engine.*
import io.ktor.client.engine.okhttp.*
import org.koin.dsl.module

actual val platformCoreModule = module {

    single<KeyValueStorage> { AndroidKeyValueStorage(context = get()) }
    single<HttpClientEngine> { OkHttp.create() }
}