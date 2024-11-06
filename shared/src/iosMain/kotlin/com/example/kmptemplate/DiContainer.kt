package com.example.kmptemplate

import com.example.kmptemplate.repository.SampleRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object DiContainer : KoinComponent {
    val sampleRepository: SampleRepository by inject()
}
