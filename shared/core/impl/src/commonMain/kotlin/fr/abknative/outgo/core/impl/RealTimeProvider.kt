package fr.abknative.outgo.core.impl

import fr.abknative.outgo.core.api.EpochMillis
import fr.abknative.outgo.core.api.TimeProvider
import kotlinx.datetime.*
import kotlinx.datetime.number
import kotlin.time.Clock
import kotlin.time.Duration.Companion.days
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
class RealTimeProvider : TimeProvider {

    private val timeZone = TimeZone.currentSystemDefault()

    override fun now(): EpochMillis = Clock.System.now().toEpochMilliseconds()

    override fun dayOfMonth(ts: EpochMillis): Int =
        Instant.fromEpochMilliseconds(ts).toLocalDateTime(timeZone).day

    override fun monthValue(ts: EpochMillis): Int =
        Instant.fromEpochMilliseconds(ts).toLocalDateTime(timeZone).month.number

    override fun yearValue(ts: EpochMillis): Int =
        Instant.fromEpochMilliseconds(ts).toLocalDateTime(timeZone).year

    override fun plusDays(base: EpochMillis, days: Int): EpochMillis =
        Instant.fromEpochMilliseconds(base).plus(days.days).toEpochMilliseconds()

    override fun minusDays(base: EpochMillis, days: Int): EpochMillis =
        Instant.fromEpochMilliseconds(base).minus(days.days).toEpochMilliseconds()

    override fun startOfMonth(ts: EpochMillis): EpochMillis {
        val date = Instant.fromEpochMilliseconds(ts).toLocalDateTime(timeZone).date
        return LocalDateTime(date.year, date.month.number, 1, 0, 0)
            .toInstant(timeZone).toEpochMilliseconds()
    }

    override fun endOfMonth(ts: EpochMillis): EpochMillis {
        val start = Instant.fromEpochMilliseconds(startOfMonth(ts)).toLocalDateTime(timeZone).date
        val nextMonth = start.plus(1, DateTimeUnit.MONTH)
        return nextMonth.atStartOfDayIn(timeZone).toEpochMilliseconds()
    }

    override fun hourOf(ts: EpochMillis): Int =
        Instant.fromEpochMilliseconds(ts).toLocalDateTime(timeZone).hour

    override fun minuteOf(ts: EpochMillis): Int =
        Instant.fromEpochMilliseconds(ts).toLocalDateTime(timeZone).minute

    override fun combineDateAndTime(dateEpochMillis: EpochMillis, hour: Int, minute: Int): EpochMillis {
        val date = Instant.fromEpochMilliseconds(dateEpochMillis).toLocalDateTime(timeZone).date
        return LocalDateTime(date.year, date.month.number, date.day, hour, minute)
            .toInstant(timeZone).toEpochMilliseconds()
    }
}