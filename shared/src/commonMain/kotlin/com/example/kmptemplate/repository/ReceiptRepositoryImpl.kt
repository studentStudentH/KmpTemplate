package com.example.kmptemplate.repository

import com.example.kmptemplate.datasource.ReceiptDataSource
import com.example.kmptemplate.datasource.ReceiptInput
import com.example.kmptemplate.domainmodel.FeeCategory
import com.example.kmptemplate.domainmodel.KmpError
import com.example.kmptemplate.domainmodel.KmpResult
import com.example.kmptemplate.domainmodel.Receipt
import com.example.kmptemplate.domainmodel.ReceiptCollection
import com.example.kmptemplate.domainmodel.YearMonth
import com.example.kmptemplate.domainmodel.convertType
import com.example.kmptemplate.util.KermitLogger
import kotlinx.datetime.Instant

internal class ReceiptRepositoryImpl(
    private val receiptDataSource: ReceiptDataSource
): ReceiptRepository {
    override suspend fun getAllReceipts(): KmpResult<ReceiptCollection> {
        return tryApiRequest("getAllReceipts") {
            val result = receiptDataSource.getAllReceipts()
            result.convertType { ReceiptCollection(it) }
        }
    }

    override suspend fun getReceiptsNewerThan(year: Int, month: Int): KmpResult<ReceiptCollection> {
        return tryApiRequest("getReceiptsNewerThan") {
            val result = receiptDataSource.getAllReceipts()
            val yearMonth = YearMonth(year, month)
            result.convertType {
                val receiptCollection = ReceiptCollection(it)
                receiptCollection.filterNewerThan(yearMonth)
            }
        }
    }

    override suspend fun getReceiptsBetween(
        oldestYear: Int,
        oldestMonth: Int,
        newestYear: Int,
        newestMonth: Int
    ): KmpResult<ReceiptCollection> {
        return tryApiRequest("getReceiptsBetween") {
            val result = receiptDataSource.getAllReceipts()
            val oldestYearMonth = YearMonth(oldestYear, oldestMonth)
            val newestYearMonth = YearMonth(newestYear, newestMonth)
            result.convertType {
                val receiptCollection = ReceiptCollection(it)
                receiptCollection
                    .filterOlerThan(oldestYearMonth)
                    .filterNewerThan(newestYearMonth)
            }
        }
    }

    override suspend fun add(
        cost: Int,
        category: FeeCategory?,
        createdAt: Instant
    ): KmpResult<Receipt> {
        return tryApiRequest("add") {
            val receiptInput = ReceiptInput(cost, category, createdAt)
            receiptDataSource.addItem(receiptInput)
        }
    }

    override suspend fun update(
        receiptId: String,
        cost: Int,
        category: FeeCategory?,
        createdAt: Instant
    ): KmpResult<Receipt> {
        return tryApiRequest("update") {
            receiptDataSource.updateItem(Receipt(receiptId, cost, category, createdAt))
        }
    }

    override suspend fun delete(receipt: Receipt): KmpResult<Receipt> {
        return tryApiRequest("delete") {
            receiptDataSource.deleteReceipts(
                listOf(receipt)
            ).convertType {
                receipt
            }
        }
    }

    private suspend fun <T: Any> tryApiRequest(
        processName: String,
        process: suspend () -> KmpResult<T>
    ): KmpResult<T> {
        return try {
            process()
        } catch (e: IllegalArgumentException) {
            val msg = e.message ?: ILLEGAL_ARGUMENT_ERROR
            KermitLogger.e(TAG) { "$processName: error = $msg" }
            KmpResult.Failure(error = KmpError.IllegalArgumentError(msg))
        } catch (e: Exception) {
            val msg = e.message ?: SERVER_ERROR
            KermitLogger.e(TAG) { "$processName: error = $msg" }
            KmpResult.Failure(error = KmpError.ServerError(msg))
        }
    }

    private companion object {
        const val TAG = "ReceiptRepositoryImpl"
        const val ILLEGAL_ARGUMENT_ERROR = "IllegalArgumentExceptionが発生しました"
        const val SERVER_ERROR = "サーバーエラーが発生しました"
    }
}
