package fr.abknative.outgo.app.di

import fr.abknative.outgo.database.DatabaseDriverFactory
import org.koin.dsl.module

val iosAppModule = module {
    single { DatabaseDriverFactory() }
}