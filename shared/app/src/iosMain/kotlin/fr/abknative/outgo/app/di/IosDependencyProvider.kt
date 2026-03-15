package fr.abknative.outgo.app.di

import fr.abknative.outgo.outgoing.api.presenter.OutgoingPresenter
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

object IosDependencyProvider : KoinComponent {

    val outgoingPresenter: OutgoingPresenter
        get() = get()

    /**
     * Point d'entrée déterministe pour initialiser Koin depuis iOS.
     */
    fun initializeKoin() {
        initKoin {}
    }
}