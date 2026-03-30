package fr.abknative.outgo.core.api

interface IdProvider {
    fun generate(): String
}