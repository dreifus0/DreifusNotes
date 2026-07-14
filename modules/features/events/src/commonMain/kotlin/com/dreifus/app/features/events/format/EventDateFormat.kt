package com.dreifus.app.features.events.format

import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.todayIn

/**
 * Display formatting for event moments. English-only for now — matches the rest of the app
 * (the notes list formats its dates the same way).
 */
object EventDateFormat {

    /** `10:00 AM` */
    fun timeLabel(at: Long): String = dateTime(at).timeLabel()

    /** `Tomorrow · 10:00 AM`, `Thu · 7:00 PM`, `Apr 12 · 9:00 AM` — for the upcoming card. */
    fun upcomingLabel(at: Long): String {
        val dt = dateTime(at)
        val prefix = when {
            dt.date.isToday() -> "Today"
            dt.date.isTomorrow() -> "Tomorrow"
            dt.date.isWithinWeek() -> dt.date.weekdayShort()
            else -> dt.date.monthDayLabel()
        }
        return "$prefix · ${dt.timeLabel()}"
    }

    /** `Thu · 7:00 PM` — for rows in the "This week" section. */
    fun weekdayTimeLabel(at: Long): String = dateTime(at).let { "${it.date.weekdayShort()} · ${it.timeLabel()}" }

    /** `Apr 12 · 9:00 AM` — for rows in the "Later" section and the archive. */
    fun dateTimeLabel(at: Long): String = dateTime(at).let { "${it.date.monthDayLabel()} · ${it.timeLabel()}" }

    /** `Tomorrow, Apr 10` / `Thu, Apr 10` / `Apr 10` — for the Date row in the editor. */
    fun dateRowLabel(at: Long): String {
        val date = dateTime(at).date
        return when {
            date.isToday() -> "Today, ${date.monthDayLabel()}"
            date.isTomorrow() -> "Tomorrow, ${date.monthDayLabel()}"
            date.isWithinWeek() -> "${date.weekdayShort()}, ${date.monthDayLabel()}"
            else -> date.monthDayLabel()
        }
    }

    fun isToday(at: Long): Boolean = dateTime(at).date.isToday()

    fun isTomorrow(at: Long): Boolean = dateTime(at).date.isTomorrow()

    fun isWithinWeek(at: Long): Boolean = dateTime(at).date.isWithinWeek()

    private fun dateTime(at: Long): LocalDateTime =
        Instant.fromEpochMilliseconds(at).toLocalDateTime(TimeZone.currentSystemDefault())

    private fun today(): LocalDate = Clock.System.todayIn(TimeZone.currentSystemDefault())

    private fun LocalDate.isToday() = this == today()

    private fun LocalDate.isTomorrow() = this == today().plus(1, DateTimeUnit.DAY)

    private fun LocalDate.isWithinWeek() = this <= today().plus(6, DateTimeUnit.DAY)

    private fun LocalDateTime.timeLabel(): String {
        val amPm = if (hour < 12) "AM" else "PM"
        val hour12 = when {
            hour == 0 -> 12
            hour > 12 -> hour - 12
            else -> hour
        }
        return "$hour12:${minute.toString().padStart(2, '0')} $amPm"
    }

    private fun LocalDate.weekdayShort(): String = dayOfWeek.name.take(3).titleCase()

    private fun LocalDate.monthDayLabel(): String = "${month.name.take(3).titleCase()} $dayOfMonth"

    private fun String.titleCase(): String = lowercase().replaceFirstChar { it.uppercase() }
}
