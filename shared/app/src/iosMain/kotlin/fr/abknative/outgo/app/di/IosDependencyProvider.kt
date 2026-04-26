package fr.abknative.outgo.app.di

import fr.abknative.outgo.core.api.KeyValueStorage
import fr.abknative.outgo.core.api.nav.NavCoordinator
import fr.abknative.outgo.list.api.ListPresenter
import fr.abknative.outgo.login.api.LoginPresenter
import fr.abknative.outgo.month.api.MonthPresenter
import fr.abknative.outgo.onboarding.api.OnboardingPresenter
import fr.abknative.outgo.operation.api.OperationPresenter
import fr.abknative.outgo.settings.api.SettingsPresenter
import fr.abknative.outgo.shell.api.ShellPresenter
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

object IosDependencyProvider : KoinComponent {

    // --- Core & Nav ---
    val navCoordinator: NavCoordinator
        get() = get()

    val keyValueStorage: KeyValueStorage
        get() = get()

    // --- Presenters ---

    val listPresenter: ListPresenter
        get() = get()

    val loginPresenter: LoginPresenter
        get() = get()

    val monthPresenter: MonthPresenter
        get() = get()

    val onboardingPresenter: OnboardingPresenter
        get() = get()

    val operationPresenter: OperationPresenter
        get() = get()

    val settingsPresenter: SettingsPresenter
        get() = get()

    val shellPresenter: ShellPresenter
        get() = get()

    // --- Initialisation ---
    fun initializeKoin() {
        initKoin {
            modules(iosAppModule)
        }
    }
}