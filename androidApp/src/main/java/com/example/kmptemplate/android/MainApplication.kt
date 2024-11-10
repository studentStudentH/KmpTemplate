package com.example.kmptemplate.android

import android.app.Application
import com.example.kmptemplate.makePlatformModule
import com.example.kmptemplate.shareDiModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MainApplication() : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@MainApplication)
            modules(
                shareDiModule,
                makePlatformModule(this@MainApplication),
                appDiModule
            )
        }
    }
}
