package fr.abknative.outgo.dashboard.impl.mock

import fr.abknative.outgo.core.api.EpochMillis
import fr.abknative.outgo.core.api.TimeProvider

class FakeTimeProvider : TimeProvider {
    var mockedNow: EpochMillis = 0L
    var mockedDay: Int = 1
    var mockedMonth: Int = 1
    var mockedLastDay: Int = 31

    override fun now(): EpochMillis = mockedNow
    override fun dayOfMonth(ts: EpochMillis): Int = mockedDay
    override fun monthValue(ts: EpochMillis): Int = mockedMonth
    override fun lastDayOfMonth(ts: EpochMillis): Int = mockedLastDay

    override fun yearValue(ts: EpochMillis): Int = 2026
    override fun hourOf(ts: EpochMillis): Int = 0
    override fun minuteOf(ts: EpochMillis): Int = 0
    override fun plusDays(base: EpochMillis, days: Int): EpochMillis = base
    override fun minusDays(base: EpochMillis, days: Int): EpochMillis = base

    override fun startOfMonth(ts: EpochMillis): EpochMillis = 0L
    override fun endOfMonth(ts: EpochMillis): EpochMillis = 0L
    override fun startOfMonth(month: Int, year: Int): EpochMillis = 0L
    override fun endOfMonth(month: Int, year: Int): EpochMillis = 0L

    override fun isSameDay(ts1: EpochMillis, ts2: EpochMillis): Boolean = ts1 == ts2
    override fun isWeekend(ts: EpochMillis): Boolean = false
    override fun combineDateAndTime(dateEpochMillis: EpochMillis, hour: Int, minute: Int): EpochMillis = 0L
}
