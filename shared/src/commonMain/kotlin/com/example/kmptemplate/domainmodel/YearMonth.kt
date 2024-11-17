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

        companion object {
            fun makeCurrentYearMonth(): YearMonth {
                val localDateTime = Clock.System.now().toSystemLocalDateTime()
                return YearMonth(localDateTime.year, localDateTime.monthNumber)
            }
        }
    }
