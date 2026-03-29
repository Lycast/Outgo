package fr.abknative.outgo.login.impl.di

import fr.abknative.outgo.login.api.LoginPresenter
import fr.abknative.outgo.login.impl.LoginPresenterImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val loginPresentationModule = module {
    viewModelOf(::LoginPresenterImpl) { bind<LoginPresenter>() }
}