package fr.abknative.outgo.android

import android.app.Application
import fr.abknative.outgo.android.di.androidAppModule
import fr.abknative.outgo.app.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

class OutgoApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        initKoin {
            androidLogger()
            androidContext(this@OutgoApplication)
            modules(androidAppModule)
        }
    }
}