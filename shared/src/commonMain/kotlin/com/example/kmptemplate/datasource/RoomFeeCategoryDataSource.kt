package com.example.kmptemplate.datasource

import com.example.kmptemplate.database.FeeCategoryDao
import com.example.kmptemplate.domainmodel.FeeCategory
import com.example.kmptemplate.domainmodel.KmpError
import com.example.kmptemplate.domainmodel.KmpResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock

/**
 * ToDo: エラーハンドリングが適当すぎるのでちゃんとする
 */
class RoomFeeCategoryDataSource(
    private val feeCategoryDao: FeeCategoryDao,
) : FeeCategoryDataSource {
    override suspend fun getAllCategory(): KmpResult<List<FeeCategory>> {
        return withContext(Dispatchers.IO) {
            try {
                val list = feeCategoryDao.loadAll()
                KmpResult.Success(list)
            } catch (e: Exception) {
                KmpResult.Failure(KmpError.ServerError(e.message ?: "不明なエラーです"))
            }
        }
    }

    override suspend fun addCategory(feeCategoryInputs: List<FeeCategoryInput>): KmpResult<List<FeeCategory>> {
        return withContext(Dispatchers.IO) {
            try {
                val convertedItems =
                    feeCategoryInputs.map {
                        FeeCategory(name = it.name, lastUsedAt = it.lastUsedAt)
                    }
                feeCategoryDao.insert(convertedItems)
                val queryNames = feeCategoryInputs.map { it.name }
                val addedItems = feeCategoryDao.loadByNames(queryNames)
                KmpResult.Success(addedItems)
            } catch (e: Exception) {
                KmpResult.Failure(KmpError.ServerError(e.message ?: "不明なエラーです"))
            }
        }
    }

    override suspend fun updateLastUsedTime(categoryId: Int): KmpResult<FeeCategory> {
        return withContext(Dispatchers.IO) {
            try {
                val data = feeCategoryDao.loadById(categoryId)
                val updatedData = data.copy(lastUsedAt = Clock.System.now())
                feeCategoryDao.update(listOf(updatedData))
                KmpResult.Success(updatedData)
            } catch (e: Exception) {
                KmpResult.Failure(KmpError.ServerError(e.message ?: "不明なエラーです"))
            }
        }
    }

    override suspend fun renameCategory(
        categoryId: Int,
        newName: String,
    ): KmpResult<FeeCategory> {
        return withContext(Dispatchers.IO) {
            try {
                val data = feeCategoryDao.loadById(categoryId)
                val updatedData = data.copy(name = newName, lastUsedAt = Clock.System.now())
                feeCategoryDao.update(listOf(updatedData))
                KmpResult.Success(updatedData)
            } catch (e: Exception) {
                KmpResult.Failure(KmpError.ServerError(e.message ?: "不明なエラーです"))
            }
        }
    }

    override suspend fun delete(feeCategories: List<FeeCategory>): KmpResult<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                feeCategoryDao.delete(feeCategories)
                KmpResult.Success(Unit)
            } catch (e: Exception) {
                KmpResult.Failure(KmpError.ServerError(e.message ?: "不明なエラーです"))
            }
        }
    }
}
