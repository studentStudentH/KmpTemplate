package com.example.kmptemplate.android

import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appDiModule: Module = module {
    viewModel { MainViewModel(get()) }
}
