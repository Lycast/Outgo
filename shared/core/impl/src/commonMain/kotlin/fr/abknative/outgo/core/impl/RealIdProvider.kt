package fr.abknative.outgo.core.impl

import fr.abknative.outgo.core.api.IdProvider
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class RealIdProvider : IdProvider {
    override fun generate(): String {
        return Uuid.random().toString()
    }
}