package fr.abknative.outgo.core.api

typealias EpochMillis = Long

interface TimeProvider {

    fun now(): EpochMillis

    // --- Extraction (Local TimeZone) ---
    fun dayOfMonth(ts: EpochMillis = now()): Int
    fun monthValue(ts: EpochMillis = now()): Int
    fun yearValue(ts: EpochMillis = now()): Int
    fun hourOf(ts: EpochMillis = now()): Int
    fun minuteOf(ts: EpochMillis = now()): Int

    // --- Calculs calendaires ---
    fun lastDayOfMonth(ts: EpochMillis = now()): Int

    fun plusDays(base: EpochMillis, days: Int): EpochMillis
    fun minusDays(base: EpochMillis, days: Int): EpochMillis

    // --- Limites de périodes ---
    fun startOfMonth(ts: EpochMillis = now()): EpochMillis
    fun endOfMonth(ts: EpochMillis = now()): EpochMillis

    // --- Helpers de comparaison & UI ---
    fun isSameDay(ts1: EpochMillis, ts2: EpochMillis): Boolean
    fun isToday(ts: EpochMillis): Boolean = isSameDay(ts, now())
    fun isWeekend(ts: EpochMillis = now()): Boolean

    /** Utile pour les DateTimePickers de l'UI */
    fun combineDateAndTime(dateEpochMillis: EpochMillis, hour: Int, minute: Int): EpochMillis
}