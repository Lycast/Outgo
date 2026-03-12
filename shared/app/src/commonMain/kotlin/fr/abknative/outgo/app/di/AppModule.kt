package fr.abknative.outgo.app.di

import fr.abknative.outgo.app.nav.AppCoordinator
import fr.abknative.outgo.core.impl.di.coreModule
import fr.abknative.outgo.database.di.databaseModule
import fr.abknative.outgo.outgoing.impl.di.outgoingModule
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

val navigationModule = module {
    single { AppCoordinator() }
}

/**
 * Starts the Koin DI graph with shared modules.
 *
 * @param appDeclaration Platform-specific configuration (e.g., `androidContext`).
 */
fun initKoin(appDeclaration: KoinAppDeclaration = {}) = startKoin {
    appDeclaration()
    modules(
        navigationModule,
        coreModule,
        databaseModule(),
        outgoingModule()
    )
}

/**
 * Swift-friendly entry point for iOS.
 *
 * Provides a parameter-less signature to bypass Swift's limitations with
 * Kotlin default lambda parameters.
 */
fun initKoinIOS() = initKoin {}