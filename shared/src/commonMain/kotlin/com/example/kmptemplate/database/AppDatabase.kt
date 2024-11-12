package com.example.kmptemplate.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.TypeConverters
import com.example.kmptemplate.domainmodel.FeeCategory

@Database(entities = [FeeCategory::class], version = 1)
@ConstructedBy(AppDatabaseConstructor::class)
@TypeConverters(
    InstantConverter::class,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getFeeCategoryDao(): FeeCategoryDao
}

// The Room compiler generates the `actual` implementations.
@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}
