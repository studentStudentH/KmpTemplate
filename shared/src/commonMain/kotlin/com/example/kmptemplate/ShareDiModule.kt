package com.example.kmptemplate

import com.example.kmptemplate.database.AppDatabase
import com.example.kmptemplate.datasource.FeeCategoryDataSource
import com.example.kmptemplate.datasource.RoomFeeCategoryDataSource
import com.example.kmptemplate.repository.DevSampleRepositoryImpl
import com.example.kmptemplate.repository.FeeCategoryRepository
import com.example.kmptemplate.repository.FeeCategoryRepositoryImpl
import com.example.kmptemplate.repository.ReleaseSampleRepositoryImpl
import com.example.kmptemplate.repository.SampleRepository
import org.koin.core.module.Module
import org.koin.dsl.module

val shareDiModule: Module =
    module {
        single<SampleRepository> {
            if (BuildKonfigFlavor.getInstance() == BuildKonfigFlavor.RELEASE) {
                ReleaseSampleRepositoryImpl()
            } else {
                DevSampleRepositoryImpl()
            }
        }
        single<FeeCategoryDataSource> {
            val dao = get<AppDatabase>().getFeeCategoryDao()
            RoomFeeCategoryDataSource(dao)
        }
        single<FeeCategoryRepository> {
            FeeCategoryRepositoryImpl(get())
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
