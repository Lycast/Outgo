package fr.abknative.outgo.app.di

import fr.abknative.outgo.auth.impl.di.authModule
import fr.abknative.outgo.core.impl.di.coreModule
import fr.abknative.outgo.database.di.databaseModule
import fr.abknative.outgo.list.impl.di.listPresentationModule
import fr.abknative.outgo.login.impl.di.loginPresentationModule
import fr.abknative.outgo.month.impl.di.monthPresentationModule
import fr.abknative.outgo.onboarding.impl.di.onboardingPresentationModule
import fr.abknative.outgo.operation.impl.di.operationPresentationModule
import fr.abknative.outgo.settings.impl.di.settingsPresentationModule
import fr.abknative.outgo.shell.impl.di.shellPresentationModule
import fr.abknative.outgo.subscription.impl.di.subscriptionModule
import fr.abknative.outgo.sync.api.SyncOrchestrator
import fr.abknative.outgo.sync.impl.di.syncModule
import fr.abknative.outgo.wallet.impl.di.walletModule
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

/**
 * Starts the Koin DI graph with shared modules and initializes global background processes.
 *
 * @param appDeclaration Platform-specific configuration (e.g., `androidContext`).
 */
fun initKoin(appDeclaration: KoinAppDeclaration = {}) {
    val koinApplication = startKoin {
        appDeclaration()
        modules(
            coreModule,
            subscriptionModule,
            databaseModule(),
            syncModule(),

            // Modules métiers (Data/Domaine)
            authModule,
            walletModule,

            // Modules UI (Présentation)
            listPresentationModule,
            monthPresentationModule,
            loginPresentationModule,
            onboardingPresentationModule,
            operationPresentationModule,
            settingsPresentationModule,
            shellPresentationModule
        )
    }

    val orchestrator = koinApplication.koin.get<SyncOrchestrator>()
    orchestrator.start()
}