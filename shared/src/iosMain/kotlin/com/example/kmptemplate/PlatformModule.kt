package com.example.kmptemplate

import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.kmptemplate.database.AppDatabase
import com.example.kmptemplate.util.KermitLogger
import kotlinx.cinterop.ExperimentalForeignApi
import org.koin.core.module.Module
import org.koin.dsl.module
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

internal val platformModule: Module =
    module {
        single<RoomDatabase.Builder<AppDatabase>> {
            getDatabaseBuilder()
        }
    }

private fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
    val dbFilePath = documentDirectory() + "/my_room.db"
    return Room.databaseBuilder<AppDatabase>(
        name = dbFilePath,
    )
}

@OptIn(ExperimentalForeignApi::class)
private fun documentDirectory(): String {
    val documentDirectory =
        NSFileManager.defaultManager.URLForDirectory(
            directory = NSDocumentDirectory,
            inDomain = NSUserDomainMask,
            appropriateForURL = null,
            create = false,
            error = null,
        )
    if (documentDirectory == null) {
        KermitLogger.e("PlatformModule") { "----- documentDirectory is null -----" }
    }
    return requireNotNull(documentDirectory?.path)
}
