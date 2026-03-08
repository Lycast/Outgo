package fr.abknative.outgo.core.impl

import fr.abknative.outgo.core.api.EpochMillis
import fr.abknative.outgo.core.api.TimeProvider
import kotlinx.datetime.*
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
class RealTimeProvider : TimeProvider {

    // On utilise le fuseau local pour toutes les extractions "humaines"
    private val timeZone = TimeZone.currentSystemDefault()

    override fun now(): EpochMillis = Clock.System.now().toEpochMilliseconds()

    // --- Extraction ---

    private fun Instant.toLocal() = this.toLocalDateTime(timeZone)

    override fun dayOfMonth(ts: EpochMillis): Int =
        Instant.fromEpochMilliseconds(ts).toLocal().day

    override fun monthValue(ts: EpochMillis): Int =
        Instant.fromEpochMilliseconds(ts).toLocal().month.number

    override fun yearValue(ts: EpochMillis): Int =
        Instant.fromEpochMilliseconds(ts).toLocal().year

    // --- Calculs calendaires (La correction DST) ---

    override fun lastDayOfMonth(ts: EpochMillis): Int {
        val localDate = Instant.fromEpochMilliseconds(ts).toLocal().date
        // Pour trouver le dernier jour : on va au 1er du mois suivant et on retire 1 jour
        val nextMonth = localDate.plus(1, DateTimeUnit.MONTH)
        val firstOfNextMonth = LocalDate(nextMonth.year, nextMonth.month, 1)
        return firstOfNextMonth.minus(1, DateTimeUnit.DAY).day
    }

    override fun plusDays(base: EpochMillis, days: Int): EpochMillis =
        Instant.fromEpochMilliseconds(base)
            .toLocalDateTime(timeZone)
            .date
            .plus(days, DateTimeUnit.DAY) // Ajout calendaire (ignore les variations de secondes)
            .atStartOfDayIn(timeZone)
            .toEpochMilliseconds()

    override fun minusDays(base: EpochMillis, days: Int): EpochMillis =
        plusDays(base, -days)

    // --- Limites de périodes ---

    override fun startOfMonth(ts: EpochMillis): EpochMillis {
        val local = Instant.fromEpochMilliseconds(ts).toLocal()
        return LocalDate(local.year, local.month, 1)
            .atStartOfDayIn(timeZone)
            .toEpochMilliseconds()
    }

    override fun endOfMonth(ts: EpochMillis): EpochMillis {
        val local = Instant.fromEpochMilliseconds(ts).toLocal()
        val lastDay = lastDayOfMonth(ts)
        // On définit la fin du mois à 23:59:59 pour la précision
        return LocalDateTime(local.year, local.month, lastDay, 23, 59, 59)
            .toInstant(timeZone)
            .toEpochMilliseconds()
    }

    // --- Helpers UI ---

    override fun isSameDay(ts1: EpochMillis, ts2: EpochMillis): Boolean {
        val d1 = Instant.fromEpochMilliseconds(ts1).toLocal().date
        val d2 = Instant.fromEpochMilliseconds(ts2).toLocal().date
        return d1 == d2
    }

    override fun isWeekend(ts: EpochMillis): Boolean {
        val dayOfWeek = Instant.fromEpochMilliseconds(ts).toLocal().dayOfWeek
        return dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY
    }

    override fun hourOf(ts: EpochMillis): Int = Instant.fromEpochMilliseconds(ts).toLocal().hour

    override fun minuteOf(ts: EpochMillis): Int = Instant.fromEpochMilliseconds(ts).toLocal().minute

    override fun combineDateAndTime(dateEpochMillis: EpochMillis, hour: Int, minute: Int): EpochMillis {
        val date = Instant.fromEpochMilliseconds(dateEpochMillis).toLocal().date
        return LocalDateTime(date.year, date.month, date.day, hour, minute)
            .toInstant(timeZone)
            .toEpochMilliseconds()
    }
}