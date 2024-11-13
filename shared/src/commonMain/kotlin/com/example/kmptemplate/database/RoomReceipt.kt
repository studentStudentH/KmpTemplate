package com.example.kmptemplate.database

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.kmptemplate.domainmodel.FeeCategory
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
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val cost: Int,
    val categoryId: Int?,
    val createdAt: Instant
)