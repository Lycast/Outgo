package fr.abknative.outgo.core.impl.di

import fr.abknative.outgo.auth.api.repository.AuthRepository
import fr.abknative.outgo.core.api.AppDispatchers
import fr.abknative.outgo.core.api.TimeProvider
import fr.abknative.outgo.core.impl.RealTimeProvider
import fr.abknative.outgo.core.impl.StandardDispatchers
import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.koin.core.module.Module
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun commonCoreModule() = module {
    singleOf(::RealTimeProvider) { bind<TimeProvider>() }
    singleOf(::StandardDispatchers) { bind<AppDispatchers>() }

    single {

        val authRepository = get<AuthRepository>()

        HttpClient(get<HttpClientEngine>()) {

            install(HttpTimeout) {
                requestTimeoutMillis = 15_000 // 15 secondes
                connectTimeoutMillis = 10_000 // 10 secondes
            }

            install(HttpRequestRetry) {
                maxRetries = 3
                retryIf { _, response -> !response.status.isSuccess() }
                delayMillis { retry -> retry * 2000L } // 2s, 4s, 6s...
            }

            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    prettyPrint = true
                })
            }

            install(Auth) {
                bearer {
                    loadTokens {

                        val session = authRepository.getSession()

                        if (session != null) {
                            BearerTokens(
                                accessToken = session.token,
                                refreshToken = ""
                            )
                        } else {
                            null
                        }
                    }
                }
            }

            defaultRequest {
                url(SecretConfig.BASE_URL) // todo url pour les tests locaux
            }
        }
    }
}

expect val platformCoreModule: Module

val coreModule = module {
    includes(commonCoreModule(), platformCoreModule)
}