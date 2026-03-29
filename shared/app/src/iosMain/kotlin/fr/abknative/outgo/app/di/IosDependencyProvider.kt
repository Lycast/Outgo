package fr.abknative.outgo.app.di

import fr.abknative.outgo.app.nav.AppCoordinator
import fr.abknative.outgo.core.api.KeyValueStorage
import fr.abknative.outgo.dashboard.api.DashboardPresenter
import fr.abknative.outgo.login.api.LoginPresenter
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

object IosDependencyProvider : KoinComponent {

    val dashboardPresenter: DashboardPresenter
        get() = get()

    val loginPresenter: LoginPresenter
        get() = get()

    val appCoordinator: AppCoordinator
        get() = get()

    val keyValueStorage: KeyValueStorage
        get() = get()

    fun initializeKoin() {
        initKoin {
            modules(iosAppModule)
        }
    }
}