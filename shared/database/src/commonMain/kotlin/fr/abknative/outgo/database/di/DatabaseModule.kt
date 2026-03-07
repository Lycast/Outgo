package fr.abknative.outgo.database.di

import fr.abknative.outgo.database.DatabaseDriverFactory
import fr.abknative.outgo.database.OutgoDatabase
import org.koin.dsl.module

fun databaseModule() = module {
    single {
        val driverFactory = get<DatabaseDriverFactory>()

        OutgoDatabase(driverFactory.createDriver())
    }
}