package com.example.kmptemplate.android.uiState

import com.example.kmptemplate.domainmodel.ReceiptCollectionPerCategory
import com.example.kmptemplate.util.KermitLogger

data class CategorySummary(
    val totalCost: Int,
    val categoryName: String,
) {
    init {
        if (totalCost < 0) {
            KermitLogger.w(TAG) { "costが0未満になるのは不適切です" }
        }
    }

    private companion object {
        const val TAG = "CategorySummary"
    }
}

fun ReceiptCollectionPerCategory.makeCategorySummary(): CategorySummary {
    return CategorySummary(totalCost, categoryName)
}
