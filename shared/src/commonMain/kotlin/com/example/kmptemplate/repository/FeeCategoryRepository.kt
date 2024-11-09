package com.example.kmptemplate.repository

import com.example.kmptemplate.domainmodel.FeeCategoryCollection
import com.example.kmptemplate.domainmodel.KmpResult

interface FeeCategoryRepository {
    suspend fun getAllCategory(): KmpResult<FeeCategoryCollection>

    /**
     * 返り値は最新のすべてのFeeCategoryの一覧
     */
    suspend fun addCategory(name: String): KmpResult<FeeCategoryCollection>

    /**
     * あるカテゴリについて、そのカテゴリを使ったReceiptを新規作成したり、
     * 既存のReceiptに割り当てたりした時に呼び出す
     * そのカテゴリの最終利用時刻を現在の時刻に設定する
     * 返り値は最新のすべてのFeeCategoryの一覧ß
     */
    suspend fun updateLastUsedTime(name: String): KmpResult<FeeCategoryCollection>
}