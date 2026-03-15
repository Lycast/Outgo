package fr.abknative.outgo.app.di

import fr.abknative.outgo.app.nav.AppCoordinator
import fr.abknative.outgo.core.api.KeyValueStorage
import fr.abknative.outgo.outgoing.api.presenter.OutgoingPresenter
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

object IosDependencyProvider : KoinComponent {

    val outgoingPresenter: OutgoingPresenter
        get() = get()

    val appCoordinator: AppCoordinator
        get() = get()

    val keyValueStorage: KeyValueStorage
        get() = get()

    /**
     * Point d'entrée déterministe pour initialiser Koin depuis iOS.
     */
    fun initializeKoin() {
        initKoin {
            modules(iosAppModule)
        }
    }
}