package com.example.kmptemplate.datasource

import com.example.kmptemplate.domainmodel.FeeCategory
import com.example.kmptemplate.domainmodel.KmpResult

interface FeeCategoryDataSource {
    suspend fun getAllCategory(): KmpResult<List<FeeCategory>>

    suspend fun addCategory(feeCategoryInputs: List<FeeCategoryInput>): KmpResult<List<FeeCategory>>

    /**
     * あるカテゴリについて、そのカテゴリを使ったReceiptを新規作成したり、
     * 既存のReceiptに割り当てたりした時に呼び出す
     * そのカテゴリの最終利用時刻を現在の時刻に設定する
     * 返り値は追加されたカテゴリ
     */
    suspend fun updateLastUsedTime(categoryId: Int): KmpResult<FeeCategory>

    /**
     * 既存のカテゴリのカテゴリ名を修正する。oldNameが登録されていなかった場合はエラー
     * 返り値は更新後のカテゴリ
     */
    suspend fun renameCategory(
        categoryId: Int,
        newName: String,
    ): KmpResult<FeeCategory>

    suspend fun delete(feeCategories: List<FeeCategory>): KmpResult<Unit>
}
