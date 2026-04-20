package fr.abknative.outgo.android.app

import android.app.Application
import fr.abknative.outgo.android.app.di.androidAppModule
import fr.abknative.outgo.app.di.initKoin
import fr.abknative.outgo.core.api.logs.AppLogger
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

class OutgoApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        AppLogger.initialize(AndroidLogger())

        initKoin {
            androidLogger()
            androidContext(this@OutgoApplication)
            modules(androidAppModule)
        }
    }
}