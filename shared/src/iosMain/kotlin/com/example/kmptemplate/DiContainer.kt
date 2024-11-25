package com.example.kmptemplate

import com.example.kmptemplate.repository.FeeCategoryRepository
import com.example.kmptemplate.repository.ReceiptRepository
import com.example.kmptemplate.repository.SampleRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object DiContainer : KoinComponent {
    val sampleRepository: SampleRepository by inject()
    val feeCategoryRepository: FeeCategoryRepository by inject()
    val receiptRepository: ReceiptRepository by inject()
}
