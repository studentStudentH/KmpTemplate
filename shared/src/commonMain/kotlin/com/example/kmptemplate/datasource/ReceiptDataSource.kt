package com.example.kmptemplate.datasource

import com.example.kmptemplate.domainmodel.KmpResult
import com.example.kmptemplate.domainmodel.Receipt

internal interface ReceiptDataSource {
    suspend fun getAllReceipts(): KmpResult<List<Receipt>>

    suspend fun addItem(receiptInput: ReceiptInput): KmpResult<Receipt>

    suspend fun updateItem(receipt: Receipt): KmpResult<Receipt>

    suspend fun deleteReceipts(receipts: List<Receipt>): KmpResult<Unit>
}
