package fr.abknative.outgo.android.di

import fr.abknative.outgo.database.DatabaseDriverFactory
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val androidAppModule = module {
    single { DatabaseDriverFactory(context = androidContext()) }
}