package fr.abknative.outgo.settings.impl.di

import fr.abknative.outgo.settings.api.SettingsPresenter
import fr.abknative.outgo.settings.impl.SettingsPresenterImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val settingsPresentationModule = module {
    viewModelOf(::SettingsPresenterImpl) { bind<SettingsPresenter>() }
}