package com.example.kmptemplate.domainmodel

import com.example.kmptemplate.util.toSystemLocalDateTime
import kotlinx.datetime.Instant

data class ReceiptCollection(
    private val receipts: List<Receipt>,
) {
    val totalCost: Int
        get() = receipts.sumOf { it.cost }

    val firstInstant: Instant
        get() = receipts.minOf { it.createdAt }

    val lastInstant: Instant
        get() = receipts.maxOf { it.createdAt }

    /**
     * 同じカテゴリのレシートをまとめたコレクションを返す
     * コレクションはtotalCostの高い順に並んでいる
     */
    fun splitByCategory(): List<ReceiptCollectionPerCategory> {
        val dict = receipts.groupBy { it.category }
        val unsortedList = dict.map { ReceiptCollectionPerCategory(it.value) }
        return unsortedList.sortedByDescending { it.totalCost }
    }

    /**
     * 時刻順にソートしてアイテムのリストを返す
     */
    fun sortByInstantDecending(): List<Receipt> {
        return receipts.sortedByDescending { it.createdAt }
    }

    /**
     * このコレクションの一番古いデータの作成日の月の前の月を返す
     */
    fun getPrevYearMonth(): YearMonth {
        val firstLocalDateTime = firstInstant.toSystemLocalDateTime()
        val year =
            if (firstLocalDateTime.monthNumber == 1) {
                firstLocalDateTime.year - 1
            } else {
                firstLocalDateTime.year
            }
        val month =
            if (firstLocalDateTime.monthNumber == 1) {
                12
            } else {
                firstLocalDateTime.monthNumber - 1
            }
        return YearMonth(year, month)
    }

    /**
     * このコレクションの一番新しいデータの作成日の月の次の月を返す
     */
    fun getNextYearMonth(): YearMonth {
        val lastLocalDateTime = lastInstant.toSystemLocalDateTime()
        val year =
            if (lastLocalDateTime.monthNumber == 12) {
                lastLocalDateTime.year + 1
            } else {
                lastLocalDateTime.year
            }
        val month =
            if (lastLocalDateTime.monthNumber == 12) {
                1
            } else {
                lastLocalDateTime.monthNumber + 1
            }
        return YearMonth(year, month)
    }
}
