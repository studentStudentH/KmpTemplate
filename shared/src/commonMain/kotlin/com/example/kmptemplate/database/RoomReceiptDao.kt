package com.example.kmptemplate.database

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.kmptemplate.domainmodel.FeeCategory

internal interface RoomReceiptDao {
    @Query("""
        SELECT * FROM RoomReceipt JOIN FeeCategory
        ON RoomReceipt.categoryId = FeeCategory.id 
        WHERE id = :receiptId
    """)
    suspend fun loadById(receiptId: Int): Map<RoomReceipt, FeeCategory>

    @Query("SELECT * FROM RoomReceipt JOIN FeeCategory ON RoomReceipt.categoryId = FeeCategory.id")
    suspend fun loadAll(): Map<RoomReceipt, FeeCategory>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(receipts: List<RoomReceipt>)

    @Update
    suspend fun update(receipts: List<RoomReceipt>)

    @Delete
    suspend fun delete(receipts: List<RoomReceipt>)
}
