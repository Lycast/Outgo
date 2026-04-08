package fr.abknative.outgo.shell.impl.di

import fr.abknative.outgo.shell.api.ShellPresenter
import fr.abknative.outgo.shell.impl.ShellPresenterImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val shellPresentationModule = module {
    viewModelOf(::ShellPresenterImpl) { bind<ShellPresenter>() }
}