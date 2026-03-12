package fr.abknative.outgo.core.impl

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import fr.abknative.outgo.core.api.KeyValueStorage

class AndroidKeyValueStorage(context: Context) : KeyValueStorage {
    private val prefs: SharedPreferences = context.getSharedPreferences("outgo_prefs", Context.MODE_PRIVATE)

    override fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return prefs.getBoolean(key, defaultValue)
    }

    override fun putBoolean(key: String, value: Boolean) {
        prefs.edit { putBoolean(key, value) }
    }
}