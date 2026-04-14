package fr.abknative.outgo.core.impl

import com.russhwolf.settings.ExperimentalSettingsImplementation
import com.russhwolf.settings.KeychainSettings
import fr.abknative.outgo.core.api.KeyValueStorage

@OptIn(ExperimentalSettingsImplementation::class)
class IosKeyValueStorage : KeyValueStorage {

    // Utilisation du Keychain iOS (Trousseau d'accès)
    private val settings = KeychainSettings(service = "OutgoSecureStorage")

    override fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return settings.getBoolean(key, defaultValue)
    }

    override fun putBoolean(key: String, value: Boolean) {
        settings.putBoolean(key, value)
    }

    override fun getLong(key: String, defaultValue: Long): Long {
        return settings.getLong(key, defaultValue)
    }

    override fun putLong(key: String, value: Long) {
        settings.putLong(key, value)
    }

    override fun getString(key: String): String? {
        return settings.getStringOrNull(key)
    }

    override fun putString(key: String, value: String) {
        settings.putString(key, value)
    }

    override fun remove(key: String) {
        settings.remove(key)
    }

    override fun clearAll() {
        settings.clear()
    }
}