package fr.abknative.outgo.dashboard.impl.mock

import fr.abknative.outgo.core.api.KeyValueStorage

class FakeKeyValueStorage : KeyValueStorage {
    private val storage = mutableMapOf<String, Any>()

    override fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return storage[key] as? Boolean ?: defaultValue
    }

    override fun putBoolean(key: String, value: Boolean) {
        storage[key] = value
    }

    override fun getLong(key: String, defaultValue: Long): Long {
        return storage[key] as? Long ?: defaultValue
    }

    override fun putLong(key: String, value: Long) {
        storage[key] = value
    }

    // Implémentations manquantes rajoutées
    override fun getString(key: String): String? {
        return storage[key] as? String
    }

    override fun putString(key: String, value: String) {
        storage[key] = value
    }

    override fun remove(key: String) {
        storage.remove(key)
    }

    override fun clearAll() {
        storage.clear()
    }
}