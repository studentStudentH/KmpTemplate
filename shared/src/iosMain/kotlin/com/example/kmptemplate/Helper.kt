package com.example.kmptemplate

import org.koin.core.context.startKoin

fun initKoin(){
    startKoin {
        modules(shareDiModule)
    }
}
