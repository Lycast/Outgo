package fr.abknative.outgo.android.app

import android.util.Log
import fr.abknative.outgo.core.api.logs.LogLevel
import fr.abknative.outgo.core.api.logs.Logger

/**
 * Implémentation réelle du Logger pour la plateforme Android.
 * Elle route les messages vers le Logcat système.
 */
class AndroidLogger : Logger {
    override fun log(level: LogLevel, tag: String, message: String, throwable: Throwable?) {
        when (level) {
            LogLevel.DEBUG -> Log.d(tag, message, throwable)
            LogLevel.INFO -> Log.i(tag, message, throwable)
            LogLevel.WARN -> Log.w(tag, message, throwable)
            LogLevel.ERROR -> Log.e(tag, message, throwable)
        }
    }
}