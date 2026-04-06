package fr.abknative.outgo.auth.impl.di

import fr.abknative.outgo.auth.api.provider.SessionProvider
import fr.abknative.outgo.auth.api.repository.AuthRepository
import fr.abknative.outgo.auth.api.usecase.*
import fr.abknative.outgo.auth.impl.AuthDataPurger
import fr.abknative.outgo.auth.impl.provider.SessionProviderImpl
import fr.abknative.outgo.auth.impl.repository.AuthRepositoryImpl
import fr.abknative.outgo.auth.impl.usecase.*
import fr.abknative.outgo.core.api.DataPurger
import fr.abknative.outgo.core.api.LocalDataDowngrader
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val authModule = module {

    singleOf(::AuthRepositoryImpl) { bind<AuthRepository>() }
    singleOf(::SessionProviderImpl) { bind <SessionProvider>() }

    factoryOf(::LoginUseCaseImpl) { bind<LoginUseCase>() }
    factoryOf(::LogoutUseCaseImpl) { bind<LogoutUseCase>() }
    factoryOf(::ObserveUserSessionUseCaseImpl) { bind<ObserveUserSessionUseCase>() }
    factoryOf(::RegisterUseCaseImpl) { bind<RegisterUseCase>() }

    factory<DeleteAccountUseCase> {
        DeleteAccountUseCaseImpl(
            authRepository = get(),
            httpClient = get(),
            localDataPurgers = getAll<DataPurger>(),
            downgraders = getAll<LocalDataDowngrader>()
        )
    }

    singleOf(::AuthDataPurger) { bind <DataPurger>() }
}