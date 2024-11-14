package com.example.kmptemplate.database

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.kmptemplate.domainmodel.FeeCategory
import com.example.kmptemplate.domainmodel.Receipt
import kotlinx.datetime.Instant

/**
 * Roomに登録する時に使うようのクラス
 */
@Entity(
    foreignKeys = [
        ForeignKey(
            entity = FeeCategory::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("categoryId"),
            onDelete = ForeignKey.SET_NULL
        )
    ]
)
internal data class RoomReceipt(
    @PrimaryKey(autoGenerate = false) val id: String,
    val cost: Int,
    val categoryId: Int?,
    val createdAt: Instant
) {
    fun toDomainModel(feeCategory: FeeCategory?): Receipt {
        return Receipt(
            id = id,
            cost = cost,
            category = feeCategory,
            createdAt = createdAt
        )
    }

    companion object {
        fun fromDomainModel(receipt: Receipt): RoomReceipt {
            return RoomReceipt(
                id = receipt.id,
                cost = receipt.cost,
                categoryId = receipt.category?.id,
                createdAt = receipt.createdAt
            )
        }
    }
}