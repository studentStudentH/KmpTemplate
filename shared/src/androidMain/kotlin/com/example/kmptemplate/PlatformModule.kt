package com.example.kmptemplate

import android.app.Application
import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.kmptemplate.database.AppDatabase
import org.koin.core.module.Module
import org.koin.dsl.module

fun makePlatformModule(application: Application): Module {
    return module {
        single<AppDatabase> {
            getDatabaseBuilder(application).build()
        }
    }
}

private fun getDatabaseBuilder(ctx: Context): RoomDatabase.Builder<AppDatabase> {
    val appContext = ctx.applicationContext
    val dbFile = appContext.getDatabasePath("my_room.db")
    return Room.databaseBuilder<AppDatabase>(
        context = appContext,
        name = dbFile.absolutePath,
    )
}
