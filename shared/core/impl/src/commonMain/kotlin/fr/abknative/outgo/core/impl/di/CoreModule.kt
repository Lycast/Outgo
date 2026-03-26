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
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.koin.core.module.Module
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import org.koin.mp.KoinPlatformTools

fun commonCoreModule() = module {
    singleOf(::RealTimeProvider) { bind<TimeProvider>() }
    singleOf(::StandardDispatchers) { bind<AppDispatchers>() }

    single {
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
                        val authRepository = KoinPlatformTools.defaultContext().get().get<AuthRepository>()
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
                header(HttpHeaders.Authorization, "Bearer ${SecretConfig.DEBUG_TOKEN}")
            }
        }
    }
}

expect val platformCoreModule: Module

val coreModule = module {
    includes(commonCoreModule(), platformCoreModule)
}