package com.example.kmptemplate.domainmodel

import com.example.kmptemplate.util.toSystemLocalDateTime
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime

data class YearMonth
    @Throws(IllegalArgumentException::class)
    constructor(
        val year: Int,
        val month: Int,
    ) {
        init {
            if (year < 0) throw IllegalArgumentException("yearは0以上にしてください")
            if (month < 1 || month > 12) throw IllegalArgumentException("monthは1以上12以下にしてください")
        }

        fun toLocalDateTime(): LocalDateTime {
            return LocalDateTime(year, month, 1, 0, 0, 0)
        }

        fun makePrevMonth(): YearMonth {
            val prevYear = if (month == 1) year - 1 else year
            val prevMonth = if (month == 1) 12 else month - 1
            return YearMonth(prevYear, prevMonth)
        }

        fun makeNextMonth(): YearMonth {
            val nextYear = if (month == 12) year + 1 else year
            val nextMonth = if (month == 12) 1 else month + 1
            return YearMonth(nextYear, nextMonth)
        }

        companion object {
            fun makeCurrentYearMonth(): YearMonth {
                val localDateTime = Clock.System.now().toSystemLocalDateTime()
                return YearMonth(localDateTime.year, localDateTime.monthNumber)
            }
        }
    }
