package com.example.kmptemplate.datasource

import com.example.kmptemplate.database.RoomReceipt
import com.example.kmptemplate.database.RoomReceiptDao
import com.example.kmptemplate.domainmodel.KmpError
import com.example.kmptemplate.domainmodel.KmpResult
import com.example.kmptemplate.domainmodel.Receipt
import com.example.kmptemplate.domainmodel.chain
import com.example.kmptemplate.domainmodel.convertType

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
            KmpResult.Success(receipts)
        } catch (e: Exception) {
            KmpResult.Failure(KmpError.ServerError(e.message ?: UNKNOWN_ERROR_MSG))
        }
    }

    override suspend fun addItem(receiptInput: ReceiptInput): KmpResult<Receipt> {
        return try {
            val categoriesResult = categoryDataSource.getAllCategory()
            categoriesResult.chain { categories ->
                if (receiptInput.category.id !in categories.map { it.id }) {
                    KmpResult.Failure(KmpError.IllegalArgumentError("存在しないカテゴリidです"))
                } else {
                    val roomReceipt = receiptInput.toRoomReceiptWithRandomId()
                    receiptDao.insert(listOf(roomReceipt))
                    val targetCategory = categories.first { it.id == receiptInput.category.id }
                    val addedReceipt = roomReceipt.toDomainModel(targetCategory)
                    KmpResult.Success(addedReceipt)
                }
            }
        } catch (e: Exception) {
            KmpResult.Failure(KmpError.ServerError(e.message ?: UNKNOWN_ERROR_MSG))
        }
    }

    override suspend fun updateItem(receipt: Receipt): KmpResult<Receipt> {
        receipt.category?.let {
            val categoryUpdateResult = categoryDataSource.updateLastUsedTime(it.id)
            if (categoryUpdateResult is KmpResult.Failure) {
                return categoryUpdateResult.convertType { receipt }
            }
        }
        try {
            receiptDao.update(listOf(RoomReceipt.fromDomainModel(receipt)))
            return KmpResult.Success(receipt)
        } catch (e: Exception) {
            return KmpResult.Failure(KmpError.ServerError(e.message ?: UNKNOWN_ERROR_MSG))
        }
    }

    override suspend fun deleteReceipts(receipts: List<Receipt>): KmpResult<Unit> {
        return try {
            receiptDao.delete(receipts.map { RoomReceipt.fromDomainModel(it) })
            KmpResult.Success(Unit)
        } catch (e: Exception) {
            KmpResult.Failure(KmpError.ServerError(e.message ?: UNKNOWN_ERROR_MSG))
        }
    }

    private companion object {
        const val TAG = "RoomReceiptDataSource"
        const val UNKNOWN_ERROR_MSG = "不明なエラーです"
    }
}
