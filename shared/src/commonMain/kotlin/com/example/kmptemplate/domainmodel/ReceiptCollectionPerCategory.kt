package com.example.kmptemplate.domainmodel

/**
 * 同じカテゴリのレシートをまとめたコレクション
 * 前提として、アイテムが最低１つはあることを想定している
 */
data class ReceiptCollectionPerCategory @Throws(IllegalArgumentException::class) constructor(
    private val receipts: List<Receipt>,
) {
    init {
        if (receipts.isEmpty()) {
            throw IllegalArgumentException("ReceiptCollectionPerCategoryには最低１つはreceiptは与えてくださいß")
        }
        val categories = receipts.map { it.category }.toSet()
        if (categories.size != 1) {
            throw IllegalArgumentException("ReceiptCollectionPerCategoryは1つのカテゴリを持つreceiptsから作成する必要があります")
        }
    }

    val totalCost: Int
        get() = receipts.sumOf { it.cost }

    val categoryName: String
        get() = receipts.first().category
}
