package fr.abknative.outgo.core.impl

import fr.abknative.outgo.core.api.KeyValueStorage
import platform.Foundation.NSUserDefaults

class IosKeyValueStorage : KeyValueStorage {
    private val defaults = NSUserDefaults.standardUserDefaults

    override fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return if (defaults.objectForKey(key) != null) {
            defaults.boolForKey(key)
        } else {
            defaultValue
        }
    }

    override fun putBoolean(key: String, value: Boolean) {
        defaults.setBool(value, forKey = key)
    }

    override fun getLong(key: String, defaultValue: Long): Long {
        return if (defaults.objectForKey(key) != null) {
            defaults.stringForKey(key)?.toLongOrNull() ?: defaultValue
        } else {
            defaultValue
        }
    }

    override fun putLong(key: String, value: Long) {
        defaults.setObject(value.toString(), forKey = key)
    }

    override fun getString(key: String): String? {
        return defaults.stringForKey(key)
    }

    override fun putString(key: String, value: String) {
        defaults.setObject(value, forKey = key)
    }

    override fun remove(key: String) {
        defaults.removeObjectForKey(key)
    }

    /**
     * Clears all key-value pairs stored in the application's default domain.
     * Removes the persistent domain associated with the main bundle identifier.
     */
    override fun clearAll() {
        val bundleIdentifier = platform.Foundation.NSBundle.mainBundle.bundleIdentifier
        if (bundleIdentifier != null) {
            defaults.removePersistentDomainForName(bundleIdentifier)
        } else {
            val dictionary = defaults.dictionaryRepresentation()
            dictionary.keys.forEach { key: Any? ->
                if (key is String) {
                    defaults.removeObjectForKey(key)
                }
            }
        }
    }
}