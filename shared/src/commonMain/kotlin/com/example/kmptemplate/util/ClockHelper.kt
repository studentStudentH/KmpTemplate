package com.example.kmptemplate.util

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.char
import kotlinx.datetime.toLocalDateTime

/**
 * Instant(UTC)を取得
 */
fun getCurrentTime(): Instant {
    return Clock.System.now()
}

fun Instant.toSystemLocalDateTime(): LocalDateTime {
    return this.toLocalDateTime(TimeZone.currentSystemDefault())
}

private val customDateTimeFormat =
    LocalDateTime.Format {
        date(LocalDate.Formats.ISO)
        char(' ')
        hour()
        char(':')
        minute()
    }

fun LocalDateTime.dateTimeFormat(): String {
    return this.format(customDateTimeFormat)
}

private val customDateFormat =
    LocalDateTime.Format {
        date(LocalDate.Formats.ISO)
    }

fun LocalDateTime.dateFormat(): String {
    return this.format(customDateFormat)
}
