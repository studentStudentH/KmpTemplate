package com.example.kmptemplate.database

import com.example.kmptemplate.domainmodel.FeeCategory

internal class SimulatedRoomReceiptDao(
    private val dataHolder: SimulatedDataHolder,
): RoomReceiptDao {
    override suspend fun loadById(receiptId: String): Map<RoomReceipt, FeeCategory> {
        val receipt = dataHolder.receiptList.first { it.id == receiptId }
        val category = dataHolder.feeCategoryList.first { it.id == receipt.categoryId }
        return mapOf(receipt to category)
    }

    override suspend fun loadAll(): Map<RoomReceipt, FeeCategory> {
        val receipts = dataHolder.receiptList
        val categories = dataHolder.feeCategoryList
        val map = mutableMapOf<RoomReceipt, FeeCategory>()
        receipts.forEach { receipt ->
            val category = categories.first { it.id == receipt.categoryId }
            map[receipt] = category
        }
        return map
    }

    override suspend fun insert(receipts: List<RoomReceipt>) {
        dataHolder.receiptList += receipts
    }

    /**
     * 計算量O(N^2)かかる実装なので大量のデータを扱うなら注意
     */
    override suspend fun update(receipts: List<RoomReceipt>) {
        receipts.forEach {
            updateOne(it)
        }
    }

    private fun updateOne(receipt: RoomReceipt) {
        val newReceipts = dataHolder.receiptList.map {
            if (it.id == receipt.id) {
                receipt
            } else {
                it
            }
        }
        dataHolder.receiptList = newReceipts
    }

    override suspend fun delete(receipts: List<RoomReceipt>) {
        val receiptIds = receipts.map { it.id }
        val newReceipts = dataHolder.receiptList.filter { it.id !in receiptIds }
        dataHolder.receiptList = newReceipts
    }
}
