package fr.abknative.outgo.core.api

typealias EpochMillis = Long

/**
 * Abstraction for time-related operations.
 * Allows for deterministic unit testing by decoupling time retrieval from the system clock.
 * Unless specified otherwise, all extractions and calculations are resolved
 * in the user's local timezone.
 */
interface TimeProvider {

    /** Returns the current timestamp in milliseconds since the Unix epoch. */
    fun now(): EpochMillis

    // --- Extraction (Local TimeZone) ---

    fun dayOfMonth(ts: EpochMillis = now()): Int
    fun monthValue(ts: EpochMillis = now()): Int
    fun yearValue(ts: EpochMillis = now()): Int
    fun hourOf(ts: EpochMillis = now()): Int
    fun minuteOf(ts: EpochMillis = now()): Int

    // --- Calendar Calculations ---

    /** Returns the last physical day of the month for the given timestamp (e.g., 28, 30, or 31). */
    fun lastDayOfMonth(ts: EpochMillis = now()): Int

    /** Safely adds calendar days, handling Daylight Saving Time (DST) shifts correctly. */
    fun plusDays(base: EpochMillis, days: Int): EpochMillis
    fun minusDays(base: EpochMillis, days: Int): EpochMillis

    // --- Period Boundaries ---

    /** Returns the timestamp corresponding to the very beginning of the month (00:00:00). */
    fun startOfMonth(ts: EpochMillis = now()): EpochMillis

    /** Returns the timestamp corresponding to the very end of the month (23:59:59). */
    fun endOfMonth(ts: EpochMillis = now()): EpochMillis

    // --- Comparison & UI Helpers ---
    fun isSameDay(ts1: EpochMillis, ts2: EpochMillis): Boolean
    fun isToday(ts: EpochMillis): Boolean = isSameDay(ts, now())
    fun isWeekend(ts: EpochMillis = now()): Boolean

    /** * Combines a specific date with a specific time.
     * Useful for parsing UI DateTimePickers results.
     */
    fun combineDateAndTime(dateEpochMillis: EpochMillis, hour: Int, minute: Int): EpochMillis
}