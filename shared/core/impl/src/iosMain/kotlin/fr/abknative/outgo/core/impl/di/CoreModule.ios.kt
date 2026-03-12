package fr.abknative.outgo.core.impl.di

import fr.abknative.outgo.core.api.KeyValueStorage
import fr.abknative.outgo.core.impl.IosKeyValueStorage
import org.koin.dsl.module

actual val platformCoreModule = module {
    single<KeyValueStorage> { IosKeyValueStorage() }
}