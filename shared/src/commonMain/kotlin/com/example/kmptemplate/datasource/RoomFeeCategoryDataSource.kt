package com.example.kmptemplate.datasource

import com.example.kmptemplate.database.FeeCategoryDao
import com.example.kmptemplate.domainmodel.FeeCategory
import com.example.kmptemplate.domainmodel.KmpError
import com.example.kmptemplate.domainmodel.KmpResult

/**
 * ToDo: エラーハンドリングが適当すぎるのでちゃんとする
 */
class RoomFeeCategoryDataSource(
    private val feeCategoryDao: FeeCategoryDao
): FeeCategoryDataSource  {
    override suspend fun getAllCategory(): KmpResult<List<FeeCategory>> {
        try {
            val list = feeCategoryDao.getAll()
            return KmpResult.Success(list)
        } catch(e: Exception) {
            return KmpResult.Failure(KmpError.ServerError(e.message ?: "不明なエラーです"))
        }
    }

    override suspend fun addCategory(vararg feeCategories: FeeCategory): KmpResult<Unit> {
        try {
            feeCategoryDao.insert(*feeCategories)
            return KmpResult.Success(Unit)
        } catch (e: Exception) {
            return KmpResult.Failure(KmpError.ServerError(e.message ?: "不明なエラーですß"))
        }
    }

    override suspend fun update(vararg feeCategories: FeeCategory): KmpResult<Unit> {
        try {
            feeCategoryDao.update(*feeCategories)
            return KmpResult.Success(Unit)
        } catch (e: Exception) {
            return KmpResult.Failure(KmpError.ServerError(e.message ?: "不明なエラーですß"))
        }
    }

    override suspend fun delete(vararg feeCategories: FeeCategory): KmpResult<Unit> {
        try {
            feeCategoryDao.delete(*feeCategories)
            return KmpResult.Success(Unit)
        } catch (e: Exception) {
            return KmpResult.Failure(KmpError.ServerError(e.message ?: "不明なエラーですß"))
        }
    }
}
