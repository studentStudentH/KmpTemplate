package com.example.kmptemplate.datasource

import com.example.kmptemplate.database.RoomReceipt
import com.example.kmptemplate.domainmodel.FeeCategory
import kotlinx.datetime.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

internal data class ReceiptInput(
    val cost: Int,
    val category: FeeCategory,
    val createdAt: Instant,
) {
    init {
        if (cost < 0) throw IllegalArgumentException("costは0以上にしてください")
    }

    @OptIn(ExperimentalUuidApi::class)
    fun toRoomReceiptWithRandomId(): RoomReceipt {
        return RoomReceipt(
            id = Uuid.random().toHexString(),
            cost = cost,
            categoryId = category.id,
            createdAt = createdAt
        )
    }
}
