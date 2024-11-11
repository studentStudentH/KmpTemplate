package com.example.kmptemplate.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.kmptemplate.domainmodel.FeeCategory

@Dao
interface FeeCategoryDao {
    @Query("SELECT * FROM FeeCategory")
    suspend fun loadAll(): List<FeeCategory>

    @Insert(onConflict =OnConflictStrategy.IGNORE)
    suspend fun insert(vararg feeCategories: FeeCategory)

    @Update
    suspend fun update(vararg feeCategories: FeeCategory)

    @Delete
    suspend fun delete(vararg feeCategories: FeeCategory)
}
