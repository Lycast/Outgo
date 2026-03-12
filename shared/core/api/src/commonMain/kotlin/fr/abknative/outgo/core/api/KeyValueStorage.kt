package fr.abknative.outgo.core.api

interface KeyValueStorage {
    fun getBoolean(key: String, defaultValue: Boolean): Boolean
    fun putBoolean(key: String, value: Boolean)
}