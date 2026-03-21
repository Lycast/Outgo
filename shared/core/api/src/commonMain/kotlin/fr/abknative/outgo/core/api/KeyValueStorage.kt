package fr.abknative.outgo.core.api

/**
 * Simple key-value storage abstraction.
 * Implemented natively (SharedPreferences on Android, NSUserDefaults on iOS).
 */
interface KeyValueStorage {
    fun getBoolean(key: String, defaultValue: Boolean): Boolean
    fun putBoolean(key: String, value: Boolean)
}