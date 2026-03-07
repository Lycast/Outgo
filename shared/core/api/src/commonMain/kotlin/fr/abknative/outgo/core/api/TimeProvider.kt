package fr.abknative.outgo.core.api

typealias EpochMillis = Long

interface TimeProvider {

    fun now(): EpochMillis

    fun dayOfMonth(ts: EpochMillis = now()): Int

    fun monthValue(ts: EpochMillis = now()): Int
    fun yearValue(ts: EpochMillis = now()): Int

    fun plusDays(base: EpochMillis, days: Int): EpochMillis
    fun minusDays(base: EpochMillis, days: Int): EpochMillis

    fun startOfMonth(ts: EpochMillis = now()): EpochMillis
    fun endOfMonth(ts: EpochMillis = now()): EpochMillis

    fun hourOf(ts: EpochMillis): Int
    fun minuteOf(ts: EpochMillis): Int
    fun combineDateAndTime(dateEpochMillis: EpochMillis, hour: Int, minute: Int): EpochMillis
}