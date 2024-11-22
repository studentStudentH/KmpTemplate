package com.example.kmptemplate.domainmodel

import com.example.kmptemplate.util.toSystemLocalDateTime
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant

data class ReceiptCollection(
    private val receipts: List<Receipt>,
) {
    val totalCost: Int
        get() {
            if (receipts.isEmpty()) {
                return 0
            }
            return receipts.sumOf { it.cost }
        }

    val firstInstant: Instant
        get() {
            if (receipts.isEmpty()) {
                return YearMonth(2024, 1)
                    .toLocalDateTime()
                    .toInstant(TimeZone.currentSystemDefault())
            }
            return receipts.minOf { it.createdAt }
        }

    val lastInstant: Instant
        get() {
            if (receipts.isEmpty()) {
                return Clock.System.now()
            }
            return receipts.maxOf { it.createdAt }
        }

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
    fun sortByInstantDescending(): List<Receipt> {
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

    fun extractNewerThan(yearMonth: YearMonth): ReceiptCollection {
        val filteredList =
            receipts.filter {
                // 日時の影響をなくすために一度YearMonthに変換してから比較
                val createdAt = YearMonth.fromLocalDateTime(it.createdAt.toSystemLocalDateTime())
                createdAt.toLocalDateTime() >= yearMonth.toLocalDateTime()
            }
        return ReceiptCollection(filteredList)
    }

    fun extractOlderThan(yearMonth: YearMonth): ReceiptCollection {
        val filteredList =
            receipts.filter {
                // 日時の影響をなくすために一度YearMonthに変換してから比較
                val createdAt = YearMonth.fromLocalDateTime(it.createdAt.toSystemLocalDateTime())
                createdAt.toLocalDateTime() <= yearMonth.toLocalDateTime()
            }
        return ReceiptCollection(filteredList)
    }

    fun firstOrNull(receiptId: String): Receipt? {
        return receipts.firstOrNull { it.id == receiptId }
    }

    companion object {
        fun makeInstanceForPreview(): ReceiptCollection {
            val receipt01 = Receipt.makeInstanceForPreview(1000, "食費")
            val receipt02 = Receipt.makeInstanceForPreview(2500, "食費")
            val receipt03 = Receipt.makeInstanceForPreview(3000, "光熱費")
            val receipt04 = Receipt.makeInstanceForPreview(8020, "その他")
            return ReceiptCollection(listOf(receipt01, receipt02, receipt03, receipt04))
        }
    }
}
