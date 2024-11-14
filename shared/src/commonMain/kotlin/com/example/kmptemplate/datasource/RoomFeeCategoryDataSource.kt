package com.example.kmptemplate.datasource

import com.example.kmptemplate.database.FeeCategoryDao
import com.example.kmptemplate.domainmodel.FeeCategory
import com.example.kmptemplate.domainmodel.KmpError
import com.example.kmptemplate.domainmodel.KmpResult
import kotlinx.datetime.Clock
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

/**
 * ToDo: エラーハンドリングが適当すぎるのでちゃんとする
 */
internal class RoomFeeCategoryDataSource(
    private val feeCategoryDao: FeeCategoryDao,
) : FeeCategoryDataSource {
    override suspend fun getAllCategory(): KmpResult<List<FeeCategory>> {
        return try {
            val list = feeCategoryDao.loadAll()
            KmpResult.Success(list)
        } catch (e: Exception) {
            KmpResult.Failure(KmpError.ServerError(e.message ?: "不明なエラーです"))
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun addCategory(feeCategoryInputs: List<FeeCategoryInput>): KmpResult<List<FeeCategory>> {
        return try {
            val convertedItems =
                feeCategoryInputs.map {
                    val newId = Uuid.random().toHexString()
                    FeeCategory(id = newId,name = it.name, lastUsedAt = it.lastUsedAt)
                }
            feeCategoryDao.insert(convertedItems)
            val queryNames = feeCategoryInputs.map { it.name }
            val addedItems = feeCategoryDao.loadByNames(queryNames)
            KmpResult.Success(addedItems)
        } catch (e: Exception) {
            KmpResult.Failure(KmpError.ServerError(e.message ?: "不明なエラーです"))
        }
    }

    override suspend fun updateLastUsedTime(categoryId: String): KmpResult<FeeCategory> {
        return try {
            val data = feeCategoryDao.loadById(categoryId)
            val updatedData = data.copy(lastUsedAt = Clock.System.now())
            feeCategoryDao.update(listOf(updatedData))
            KmpResult.Success(updatedData)
        } catch (e: Exception) {
            KmpResult.Failure(KmpError.ServerError(e.message ?: "不明なエラーです"))
        }
    }

    override suspend fun renameCategory(
        categoryId: String,
        newName: String,
    ): KmpResult<FeeCategory> {
        return try {
            val data = feeCategoryDao.loadById(categoryId)
            val updatedData = data.copy(name = newName, lastUsedAt = Clock.System.now())
            feeCategoryDao.update(listOf(updatedData))
            KmpResult.Success(updatedData)
        } catch (e: Exception) {
            KmpResult.Failure(KmpError.ServerError(e.message ?: "不明なエラーです"))
        }
    }

    override suspend fun delete(feeCategories: List<FeeCategory>): KmpResult<Unit> {
        return try {
            feeCategoryDao.delete(feeCategories)
            KmpResult.Success(Unit)
        } catch (e: Exception) {
            KmpResult.Failure(KmpError.ServerError(e.message ?: "不明なエラーです"))
        }
    }
}
