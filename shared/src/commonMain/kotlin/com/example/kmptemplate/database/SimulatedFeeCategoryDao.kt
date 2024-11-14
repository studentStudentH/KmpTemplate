package com.example.kmptemplate.database

import com.example.kmptemplate.domainmodel.FeeCategory

internal class SimulatedFeeCategoryDao(
    private val dataHolder: SimulatedDataHolder
): FeeCategoryDao {
    override suspend fun loadById(categoryId: String): FeeCategory {
        return dataHolder.feeCategoryList.first { it.id == categoryId }
    }

    override suspend fun loadAll(): List<FeeCategory> {
        return dataHolder.feeCategoryList
    }

    override suspend fun loadByNames(names: List<String>): List<FeeCategory> {
        return dataHolder.feeCategoryList.filter { it.name in names }
    }

    override suspend fun insert(feeCategories: List<FeeCategory>) {
        dataHolder.feeCategoryList += feeCategories
    }

    /**
     * 計算量O(N^2)かかる実装なので大量のデータを扱うなら注意
     */
    override suspend fun update(feeCategories: List<FeeCategory>) {
        feeCategories.forEach {
            updateOne(it)
        }
    }

    private fun updateOne(feeCategory: FeeCategory) {
        val newCategories = dataHolder.feeCategoryList.map {
            if (it.id == feeCategory.id) {
                feeCategory
            } else {
                it
            }
        }
        dataHolder.feeCategoryList = newCategories
    }

    override suspend fun delete(feeCategories: List<FeeCategory>) {
        // カテゴリの更新
        val feeCategoryIds = feeCategories.map { it.id }
        val newCategories = dataHolder.feeCategoryList.filter { it.id !in feeCategoryIds }
        dataHolder.feeCategoryList = newCategories
        // レシート内のカテゴリの更新
        val deletedCategoryIds = feeCategories.map { it.id }
        val newReceipts = dataHolder.receiptList.map {
            if (it.categoryId in deletedCategoryIds) {
                it.copy(categoryId = null)
            }
            else {
                it
            }
        }
        dataHolder.receiptList = newReceipts
    }
}
