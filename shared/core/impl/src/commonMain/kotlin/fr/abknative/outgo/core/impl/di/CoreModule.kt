package fr.abknative.outgo.core.impl.di

import fr.abknative.outgo.auth.api.repository.AuthRepository
import fr.abknative.outgo.core.api.AppDispatchers
import fr.abknative.outgo.core.api.DataPurger
import fr.abknative.outgo.core.api.IdProvider
import fr.abknative.outgo.core.api.TimeProvider
import fr.abknative.outgo.core.api.usecase.ClearLocalDataUseCase
import fr.abknative.outgo.core.impl.RealIdProvider
import fr.abknative.outgo.core.impl.RealTimeProvider
import fr.abknative.outgo.core.impl.StandardDispatchers
import fr.abknative.outgo.core.impl.usecase.ClearLocalDataUseCaseImpl
import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.serialization.json.Json
import org.koin.core.module.Module
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun commonCoreModule() = module {

    singleOf(::RealTimeProvider) { bind<TimeProvider>() }
    singleOf(::StandardDispatchers) { bind<AppDispatchers>() }
    singleOf(::RealIdProvider) { bind<IdProvider>() }

    single { CoroutineScope(SupervisorJob() + get<AppDispatchers>().main) }

    factory<ClearLocalDataUseCase> { ClearLocalDataUseCaseImpl(purgers = getAll<DataPurger>(), storage = get()) }

    single {
        val authRepository: AuthRepository = get()

        HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                })
            }
            install(Auth) {
                bearer {
                    loadTokens {
                        val session = authRepository.getSession()
                        session?.token?.let { BearerTokens(it, "") }
                    }
                }
            }
            defaultRequest {
                url(SecretConfig.BASE_URL) // todo url non dynamique nécessite le swap dans secret config
                header(HttpHeaders.ContentType, ContentType.Application.Json)
            }
        }
    }
}

expect val platformCoreModule: Module

val coreModule = module {
    includes(commonCoreModule(), platformCoreModule)
}