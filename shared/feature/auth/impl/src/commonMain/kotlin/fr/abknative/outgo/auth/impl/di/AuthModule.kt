package fr.abknative.outgo.auth.impl.di

import fr.abknative.outgo.auth.api.repository.AuthRepository
import fr.abknative.outgo.auth.api.usecase.DeleteAccountUseCase
import fr.abknative.outgo.auth.api.usecase.LoginUseCase
import fr.abknative.outgo.auth.api.usecase.LogoutUseCase
import fr.abknative.outgo.auth.api.usecase.ObserveUserSessionUseCase
import fr.abknative.outgo.auth.impl.AuthDataPurger
import fr.abknative.outgo.auth.impl.repository.AuthRepositoryImpl
import fr.abknative.outgo.auth.impl.usecase.DeleteAccountUseCaseImpl
import fr.abknative.outgo.auth.impl.usecase.LoginUseCaseImpl
import fr.abknative.outgo.auth.impl.usecase.LogoutUseCaseImpl
import fr.abknative.outgo.auth.impl.usecase.ObserveUserSessionUseCaseImpl
import fr.abknative.outgo.core.api.DataPurger
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val authModule = module {

    singleOf(::AuthRepositoryImpl) { bind<AuthRepository>() }

    factoryOf(::LoginUseCaseImpl) { bind<LoginUseCase>() }
    factoryOf(::LogoutUseCaseImpl) { bind<LogoutUseCase>() }
    factoryOf(::ObserveUserSessionUseCaseImpl) { bind<ObserveUserSessionUseCase>() }

    factoryOf(::DeleteAccountUseCaseImpl) { bind<DeleteAccountUseCase>() }

    single<DataPurger> { AuthDataPurger(get()) }
}