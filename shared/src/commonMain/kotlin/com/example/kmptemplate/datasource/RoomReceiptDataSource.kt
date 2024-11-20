package com.example.kmptemplate.datasource

import com.example.kmptemplate.database.RoomReceipt
import com.example.kmptemplate.database.RoomReceiptDao
import com.example.kmptemplate.domainmodel.FeeCategory
import com.example.kmptemplate.domainmodel.KmpError
import com.example.kmptemplate.domainmodel.KmpResult
import com.example.kmptemplate.domainmodel.Receipt
import com.example.kmptemplate.domainmodel.chain
import com.example.kmptemplate.domainmodel.convertType
import com.example.kmptemplate.util.KermitLogger

internal class RoomReceiptDataSource(
    private val receiptDao: RoomReceiptDao,
    private val categoryDataSource: FeeCategoryDataSource,
) : ReceiptDataSource {
    override suspend fun getAllReceipts(): KmpResult<List<Receipt>> {
        return try {
            val receiptMaps = receiptDao.loadAll()
            val receipts =
                receiptMaps.map { (roomReceipt, feeCategory) ->
                    roomReceipt.toDomainModel(feeCategory)
                }
            KermitLogger.d(TAG) { "getAllReceipts(): receipts = $receipts" }
            KmpResult.Success(receipts)
        } catch (e: Exception) {
            val msg = e.message ?: UNKNOWN_ERROR_MSG
            KermitLogger.e(TAG) { msg }
            KmpResult.Failure(KmpError.ServerError(msg))
        }
    }

    override suspend fun addItem(receiptInput: ReceiptInput): KmpResult<Receipt> {
        try {
            if (receiptInput.category == null) {
                return addRoomReceipt(receiptInput, null)
            }
            val categoriesResult = categoryDataSource.getAllCategory()
            return categoriesResult.chain { categories ->
                if (receiptInput.category.id !in categories.map { it.id }) {
                    KermitLogger.e(TAG) {
                        "addItem(): category not found. receiptInput.category.id = ${receiptInput.category.id}"
                    }
                    KmpResult.Failure(KmpError.IllegalArgumentError("存在しないカテゴリidです"))
                } else {
                    categoryDataSource.updateLastUsedTime(receiptInput.category.id)
                }
            }.chain { targetCategory ->
                addRoomReceipt(receiptInput, targetCategory)
            }
        } catch (e: Exception) {
            val msg = e.message ?: UNKNOWN_ERROR_MSG
            KermitLogger.e(TAG) { msg }
            return KmpResult.Failure(KmpError.ServerError(msg))
        }
    }

    private suspend fun addRoomReceipt(
        receiptInput: ReceiptInput,
        targetCategory: FeeCategory?,
    ): KmpResult<Receipt> {
        val roomReceipt = receiptInput.toRoomReceiptWithRandomId()
        receiptDao.insert(listOf(roomReceipt))
        val addedReceipt = roomReceipt.toDomainModel(targetCategory)
        KermitLogger.d(TAG) { "addRoomReceipt(): addedReceipt = $addedReceipt" }
        return KmpResult.Success(addedReceipt)
    }

    override suspend fun updateItem(receipt: Receipt): KmpResult<Receipt> {
        val updatedCategory =
            receipt.category?.let {
                val categoryUpdateResult = categoryDataSource.updateLastUsedTime(it.id)
                val logMsg = "updateItem(): categoryUpdateResult = $categoryUpdateResult"
                when (categoryUpdateResult) {
                    is KmpResult.Failure -> {
                        KermitLogger.e(TAG) { logMsg }
                        return@updateItem categoryUpdateResult.convertType { receipt }
                    }
                    is KmpResult.Success -> {
                        KermitLogger.d(TAG) { logMsg }
                        return@let categoryUpdateResult.value
                    }
                }
            }
        return try {
            receiptDao.update(listOf(RoomReceipt.fromDomainModel(receipt)))
            KermitLogger.d(TAG) { "updateItem(): receipt = $receipt" }
            KmpResult.Success(receipt.copy(category = updatedCategory))
        } catch (e: Exception) {
            val msg = e.message ?: UNKNOWN_ERROR_MSG
            KermitLogger.e(TAG) { msg }
            KmpResult.Failure(KmpError.ServerError(msg))
        }
    }

    override suspend fun deleteReceipts(receipts: List<Receipt>): KmpResult<Unit> {
        return try {
            receiptDao.delete(receipts.map { RoomReceipt.fromDomainModel(it) })
            KermitLogger.d(TAG) { "deleteReceipts(): receipts = $receipts" }
            KmpResult.Success(Unit)
        } catch (e: Exception) {
            val msg = e.message ?: UNKNOWN_ERROR_MSG
            KermitLogger.e(TAG) { msg }
            KmpResult.Failure(KmpError.ServerError(msg))
        }
    }

    private companion object {
        const val TAG = "RoomReceiptDataSource"
        const val UNKNOWN_ERROR_MSG = "不明なエラーです"
    }
}
