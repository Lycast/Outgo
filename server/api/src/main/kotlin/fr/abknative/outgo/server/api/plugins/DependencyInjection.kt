package fr.abknative.outgo.server.api.plugins

import fr.abknative.outgo.server.api.di.serverModule
import io.ktor.server.application.*
import org.koin.ktor.plugin.Koin

fun Application.configureDependencyInjection() {
    install(Koin) { modules(serverModule) }
}