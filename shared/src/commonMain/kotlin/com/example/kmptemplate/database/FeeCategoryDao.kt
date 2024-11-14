package com.example.kmptemplate.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.kmptemplate.domainmodel.FeeCategory

@Dao
internal interface FeeCategoryDao {
    @Query("SELECT * FROM FeeCategory WHERE id = :categoryId")
    suspend fun loadById(categoryId: String): FeeCategory

    @Query("SELECT * FROM FeeCategory")
    suspend fun loadAll(): List<FeeCategory>

    @Query("SELECT * FROM FeeCategory WHERE name IN (:names)")
    suspend fun loadByNames(names: List<String>): List<FeeCategory>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(feeCategories: List<FeeCategory>)

    @Update
    suspend fun update(feeCategories: List<FeeCategory>)

    @Delete
    suspend fun delete(feeCategories: List<FeeCategory>)
}
