package fr.abknative.outgo.auth.impl.di

import fr.abknative.outgo.auth.api.presenter.AuthPresenter
import fr.abknative.outgo.auth.api.repository.AuthRepository
import fr.abknative.outgo.auth.api.usecase.LoginUseCase
import fr.abknative.outgo.auth.api.usecase.LogoutUseCase
import fr.abknative.outgo.auth.api.usecase.ObserveUserSessionUseCase
import fr.abknative.outgo.auth.impl.presenter.AuthPresenterImpl
import fr.abknative.outgo.auth.impl.repository.AuthRepositoryImpl
import fr.abknative.outgo.auth.impl.usecase.LoginUseCaseImpl
import fr.abknative.outgo.auth.impl.usecase.LogoutUseCaseImpl
import fr.abknative.outgo.auth.impl.usecase.ObserveUserSessionUseCaseImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val authModule = module {
    singleOf(::AuthRepositoryImpl) { bind<AuthRepository>() }

    factoryOf(::LoginUseCaseImpl) { bind<LoginUseCase>() }
    factoryOf(::LogoutUseCaseImpl) { bind<LogoutUseCase>() }
    factoryOf(::ObserveUserSessionUseCaseImpl) { bind<ObserveUserSessionUseCase>() }

    viewModelOf(::AuthPresenterImpl) { bind<AuthPresenter>() }
}