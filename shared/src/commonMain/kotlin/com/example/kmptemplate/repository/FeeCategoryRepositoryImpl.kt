package com.example.kmptemplate.repository

import com.example.kmptemplate.datasource.FeeCategoryDataSource
import com.example.kmptemplate.datasource.FeeCategoryInput
import com.example.kmptemplate.domainmodel.FeeCategory
import com.example.kmptemplate.domainmodel.FeeCategoryCollection
import com.example.kmptemplate.domainmodel.KmpError
import com.example.kmptemplate.domainmodel.KmpResult
import com.example.kmptemplate.domainmodel.chain
import com.example.kmptemplate.domainmodel.convertType
import com.example.kmptemplate.util.KermitLogger
import kotlinx.datetime.Clock

internal class FeeCategoryRepositoryImpl(
    private val dataSource: FeeCategoryDataSource,
) : FeeCategoryRepository {
    override suspend fun getAllCategory(): KmpResult<FeeCategoryCollection> {
        val result = dataSource.getAllCategory()
        when (result) {
            is KmpResult.Failure -> {} // 何もしない
            is KmpResult.Success -> {
                // 空なら初期化する
                if (result.value.isEmpty()) {
                    return initializeCategory()
                }
            }
        }
        return try {
            result.convertType {
                FeeCategoryCollection(it)
            }
        } catch (e: IllegalArgumentException) {
            val msg = e.message ?: "不明なエラーです"
            KmpResult.Failure(error = KmpError.IllegalArgumentError(msg))
        }
    }

    override suspend fun addCategory(name: String): KmpResult<FeeCategory> {
        try {
            val categoryInput = FeeCategoryInput(name, Clock.System.now())
            val result = dataSource.addCategory(listOf(categoryInput))
            return result.convertType { it.first() }
        } catch (e: IllegalArgumentException) {
            val msg = e.message ?: "不明なエラーです"
            KermitLogger.e(TAG) { "addCategory() IllegalArgumentException = $msg" }
            return KmpResult.Failure(error = KmpError.IllegalArgumentError(msg))
        } catch (e: NoSuchElementException) {
            val msg = "DataSourceからの返り値が空Listです"
            KermitLogger.e(TAG) { "addCategory() error = $msg" }
            return KmpResult.Failure(error = KmpError.ClientError(msg))
        }
    }

    override suspend fun updateLastUsedTime(categoryId: String): KmpResult<FeeCategory> {
        return dataSource.updateLastUsedTime(categoryId)
    }

    override suspend fun renameCategory(
        categoryId: String,
        newName: String,
    ): KmpResult<FeeCategory> {
        return dataSource.renameCategory(categoryId, newName)
    }

    override suspend fun deleteCategory(categoryId: String): KmpResult<Unit> {
        val getResult = this.getAllCategory()
        val feeCategoryResult: KmpResult<FeeCategory> =
            getResult.chain { feeCategoryCollection ->
                val feeCategory = feeCategoryCollection.getById(categoryId)
                if (feeCategory == null) {
                    val msg = "カテゴリが存在しません"
                    KermitLogger.e(TAG) { "deleteCategory() error = $msg" }
                    KmpResult.Failure(error = KmpError.IllegalArgumentError(msg))
                } else {
                    KmpResult.Success(feeCategory)
                }
            }
        return feeCategoryResult.chain { targetFeeCategory ->
            dataSource.delete(listOf(targetFeeCategory))
        }
    }

    private suspend fun initializeCategory(): KmpResult<FeeCategoryCollection> {
        val inputs =
            FeeCategory.INITIAL_CATEGORIES.map {
                FeeCategoryInput(it, Clock.System.now())
            }
        val result = dataSource.addCategory(inputs)
        return try {
            result.convertType { FeeCategoryCollection(it) }
        } catch (e: IllegalArgumentException) {
            val msg = "カテゴリ初期化時に発生するはずのないIllegalArgumentExceptionが発生しました"
            KermitLogger.e(TAG) { "initializeCategory() error = $msg" }
            KmpResult.Failure(error = KmpError.IllegalArgumentError(msg))
        }
    }

    private companion object {
        const val TAG = "FeeCategoryRepositoryImpl"
    }
}
