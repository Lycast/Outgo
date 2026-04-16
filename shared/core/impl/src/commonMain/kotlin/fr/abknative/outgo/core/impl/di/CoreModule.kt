package fr.abknative.outgo.core.impl.di

import fr.abknative.outgo.auth.api.repository.AuthRepository
import fr.abknative.outgo.core.api.*
import fr.abknative.outgo.core.api.logs.ExceptionMapper
import fr.abknative.outgo.core.api.nav.NavCoordinator
import fr.abknative.outgo.core.api.usecase.ClearLocalDataUseCase
import fr.abknative.outgo.core.impl.RealIdProvider
import fr.abknative.outgo.core.impl.RealTimeProvider
import fr.abknative.outgo.core.impl.StandardDispatchers
import fr.abknative.outgo.core.impl.logs.KtorExceptionMapper
import fr.abknative.outgo.core.impl.nav.NavCoordinatorImpl
import fr.abknative.outgo.core.impl.usecase.ClearLocalDataUseCaseImpl
import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.plugins.*
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
    singleOf(::NavCoordinatorImpl) { bind<NavCoordinator>() }
    singleOf(::KtorExceptionMapper) { bind<ExceptionMapper>() }

    single { CoroutineScope(SupervisorJob() + get<AppDispatchers>().main) }

    factory<ClearLocalDataUseCase> { ClearLocalDataUseCaseImpl(purgers = getAll<DataPurger>(), storage = get()) }

    single {
        val authRepository: AuthRepository = get()
        val engine: HttpClientEngine = get()

        /**
         * Configures the Ktor HttpClient.
         * The standard Auth plugin is intentionally omitted to avoid token caching issues
         * when seamlessly switching between multiple local user accounts.
         */
        val client = HttpClient(engine) {
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                })
            }
            defaultRequest {
                url(SecretConfig.BASE_URL) // todo url non dynamique nécessite le swap dans secret config
                header(HttpHeaders.ContentType, ContentType.Application.Json)
            }
        }

        /**
         * Intercepts all outgoing requests to inject a fresh authorization token directly
         * from the single source of truth (AuthRepository).
         * This guarantees that network requests are always bound to the currently active session.
         */
        client.plugin(HttpSend).intercept { request ->
            val session = authRepository.getSession()
            if (session != null) {
                request.headers.remove(HttpHeaders.Authorization)
                request.headers.append(HttpHeaders.Authorization, "Bearer ${session.token}")
            }
            execute(request)
        }

        client
    }
}

expect val platformCoreModule: Module

val coreModule = module {
    includes(commonCoreModule(), platformCoreModule)
}