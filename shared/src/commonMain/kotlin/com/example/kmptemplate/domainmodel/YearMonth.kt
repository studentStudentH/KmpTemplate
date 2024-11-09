package com.example.kmptemplate.domainmodel

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
    }
