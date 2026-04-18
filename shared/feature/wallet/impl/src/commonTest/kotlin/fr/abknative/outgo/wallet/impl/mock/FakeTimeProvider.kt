package fr.abknative.outgo.wallet.impl.mock

import fr.abknative.outgo.core.api.time.EpochMillis
import fr.abknative.outgo.core.api.time.TimeProvider

class FakeTimeProvider : TimeProvider {
    var mockedNow: EpochMillis = 1700000000000L
    var mockedDay: Int = 15
    var mockedMonth: Int = 3
    var mockedYear: Int = 2026
    var mockedLastDay: Int = 31

    var mockedStartOfMonth: EpochMillis = 1700000000000L
    var mockedEndOfMonth: EpochMillis = 1700002592000L

    override fun now(): EpochMillis = mockedNow
    override fun dayOfMonth(ts: EpochMillis): Int = mockedDay
    override fun monthValue(ts: EpochMillis): Int = mockedMonth
    override fun yearValue(ts: EpochMillis): Int = mockedYear
    override fun lastDayOfMonth(ts: EpochMillis): Int = mockedLastDay

    override fun hourOf(ts: EpochMillis): Int = 0
    override fun minuteOf(ts: EpochMillis): Int = 0

    override fun startOfMonth(ts: EpochMillis): EpochMillis = mockedStartOfMonth
    override fun endOfMonth(ts: EpochMillis): EpochMillis = mockedEndOfMonth

    // Ces deux-là sont cruciaux pour ObserveActiveOperationsUseCase
    override fun startOfMonth(month: Int, year: Int): EpochMillis = mockedStartOfMonth
    override fun endOfMonth(month: Int, year: Int): EpochMillis = mockedEndOfMonth

    override fun plusDays(base: EpochMillis, days: Int): EpochMillis = base + (days * 86400000L)
    override fun minusDays(base: EpochMillis, days: Int): EpochMillis = base - (days * 86400000L)

    override fun isSameDay(ts1: EpochMillis, ts2: EpochMillis): Boolean = ts1 == ts2
    override fun isWeekend(ts: EpochMillis): Boolean = false
    override fun combineDateAndTime(dateEpochMillis: EpochMillis, hour: Int, minute: Int): EpochMillis = dateEpochMillis
    override fun formatShortDate(millis: EpochMillis): String {
        TODO("Not yet implemented")
    }
}