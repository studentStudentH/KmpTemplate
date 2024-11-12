package com.example.kmptemplate.repository

import com.example.kmptemplate.domainmodel.FeeCategory
import com.example.kmptemplate.domainmodel.FeeCategoryCollection
import com.example.kmptemplate.domainmodel.KmpResult

interface FeeCategoryRepository {
    suspend fun getAllCategory(): KmpResult<FeeCategoryCollection>


    /**
     * 返り値は追加されたカテゴリ
     */
    suspend fun addCategory(name: String): KmpResult<FeeCategory>

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
    suspend fun renameCategory(categoryId: Int, newName: String): KmpResult<FeeCategory>

    /**
     * 既存のカテゴリを削除する。そのカテゴリが登録されていなかった場合はエラー
     * 返り値は成否のみ
     */
    suspend fun deleteCategory(categoryId: Int): KmpResult<Unit>
}
