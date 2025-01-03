package com.example.kmptemplate.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.kmptemplate.domainmodel.FeeCategory

@Dao
internal interface RoomReceiptDao {
    @Query(
        """
        SELECT * FROM RoomReceipt LEFT OUTER JOIN FeeCategory
        ON RoomReceipt.categoryId = FeeCategory.id 
        WHERE RoomReceipt.id = :receiptId
    """,
    )
    suspend fun loadById(receiptId: String): Map<RoomReceipt, FeeCategory?>

    @Query("SELECT * FROM RoomReceipt LEFT OUTER JOIN FeeCategory ON RoomReceipt.categoryId = FeeCategory.id")
    suspend fun loadAll(): Map<RoomReceipt, FeeCategory?>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(receipts: List<RoomReceipt>)

    @Update
    suspend fun update(receipts: List<RoomReceipt>)

    @Delete
    suspend fun delete(receipts: List<RoomReceipt>)
}
