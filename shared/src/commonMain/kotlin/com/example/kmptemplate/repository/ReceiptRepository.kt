package com.example.kmptemplate.repository

import com.example.kmptemplate.domainmodel.FeeCategory
import com.example.kmptemplate.domainmodel.KmpResult
import com.example.kmptemplate.domainmodel.Receipt
import com.example.kmptemplate.domainmodel.ReceiptCollection
import kotlinx.datetime.Instant

interface ReceiptRepository {
    suspend fun getAllReceipts(): KmpResult<ReceiptCollection>

    suspend fun getReceiptsNewerThan(
        year: Int,
        month: Int,
    ): KmpResult<ReceiptCollection>

    suspend fun getReceiptsBetween(
        oldestYear: Int,
        oldestMonth: Int,
        newestYear: Int,
        newestMonth: Int,
    ): KmpResult<ReceiptCollection>

    /**
     * 返り値は新規作成したReceiptが入る
     */
    suspend fun add(
        cost: Int,
        category: FeeCategory?,
        createdAt: Instant,
    ): KmpResult<Receipt>

    suspend fun update(
        receiptId: String,
        cost: Int,
        category: FeeCategory?,
        createdAt: Instant,
    ): KmpResult<Receipt>

    suspend fun delete(receipt: Receipt): KmpResult<Receipt>
}
