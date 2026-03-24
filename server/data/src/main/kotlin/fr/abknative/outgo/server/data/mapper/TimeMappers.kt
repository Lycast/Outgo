package fr.abknative.outgo.server.data.mapper

import java.time.OffsetDateTime
import java.time.ZoneOffset

internal fun Long.toSqlOffsetDateTime(): OffsetDateTime {
    return java.time.Instant.ofEpochMilli(this).atOffset(ZoneOffset.UTC)
}

internal fun Long.toExposedQueryInstant(): kotlin.time.Instant {
    return kotlin.time.Instant.fromEpochMilliseconds(this)
}

internal fun OffsetDateTime.toEpochMillis(): Long {
    return this.toInstant().toEpochMilli()
}