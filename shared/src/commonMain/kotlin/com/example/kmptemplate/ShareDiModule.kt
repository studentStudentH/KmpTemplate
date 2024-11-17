package com.example.kmptemplate

import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.example.kmptemplate.database.AppDatabase
import com.example.kmptemplate.database.FeeCategoryDao
import com.example.kmptemplate.database.RoomReceiptDao
import com.example.kmptemplate.database.SimulatedDataHolder
import com.example.kmptemplate.database.SimulatedFeeCategoryDao
import com.example.kmptemplate.database.SimulatedRoomReceiptDao
import com.example.kmptemplate.datasource.FeeCategoryDataSource
import com.example.kmptemplate.datasource.ReceiptDataSource
import com.example.kmptemplate.datasource.RoomFeeCategoryDataSource
import com.example.kmptemplate.datasource.RoomReceiptDataSource
import com.example.kmptemplate.repository.DevSampleRepositoryImpl
import com.example.kmptemplate.repository.FeeCategoryRepository
import com.example.kmptemplate.repository.FeeCategoryRepositoryImpl
import com.example.kmptemplate.repository.ReceiptRepository
import com.example.kmptemplate.repository.ReceiptRepositoryImpl
import com.example.kmptemplate.repository.ReleaseSampleRepositoryImpl
import com.example.kmptemplate.repository.SampleRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.koin.core.module.Module
import org.koin.dsl.module

val shareDiModule: Module =
    module {
        val buildKonfigFlavor = BuildKonfigFlavor.getInstance()
        val simulatedDataHolder = SimulatedDataHolder.makeTestData()
        single<SampleRepository> {
            if (BuildKonfigFlavor.getInstance() == BuildKonfigFlavor.RELEASE) {
                ReleaseSampleRepositoryImpl()
            } else {
                DevSampleRepositoryImpl()
            }
        }
        single<FeeCategoryDataSource> {
            RoomFeeCategoryDataSource(get())
        }
        single<FeeCategoryRepository> {
            FeeCategoryRepositoryImpl(get())
        }
        single<ReceiptDataSource> {
            RoomReceiptDataSource(get(), get())
        }
        single<ReceiptRepository> {
            ReceiptRepositoryImpl(get())
        }
        single<AppDatabase> {
            val builder = get<RoomDatabase.Builder<AppDatabase>>()
            builder
                .setDriver(BundledSQLiteDriver())
                .setQueryCoroutineContext(Dispatchers.IO)
                .build()
        }

        // 環境によってData Access Objectを切り替える
        single<FeeCategoryDao> {
            when (buildKonfigFlavor) {
                BuildKonfigFlavor.DEV -> SimulatedFeeCategoryDao(simulatedDataHolder)
                BuildKonfigFlavor.RELEASE -> get<AppDatabase>().getFeeCategoryDao()
            }
        }
        single<RoomReceiptDao> {
            when (buildKonfigFlavor) {
                BuildKonfigFlavor.DEV -> SimulatedRoomReceiptDao(simulatedDataHolder)
                BuildKonfigFlavor.RELEASE -> get<AppDatabase>().getRoomReceiptDao()
            }
        }
    }

private enum class BuildKonfigFlavor {
    DEV,
    RELEASE,
    ;

    companion object {
        fun getInstance(): BuildKonfigFlavor {
            val flavor = BuildKonfig.flavor
            if (flavor == "dev") {
                return DEV
            }
            if (flavor == "release") {
                return RELEASE
            }
            throw IllegalStateException("flavorの値が不適正です。BuildKonfig.flavor = $flavor")
        }
    }
}
