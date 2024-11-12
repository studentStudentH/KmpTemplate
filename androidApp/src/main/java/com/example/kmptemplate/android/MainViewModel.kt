package com.example.kmptemplate.android

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kmptemplate.domainmodel.KmpResult
import com.example.kmptemplate.repository.FeeCategoryRepository
import com.example.kmptemplate.repository.SampleRepository
import com.example.kmptemplate.util.KermitLogger
import com.example.kmptemplate.util.dateTimeFormat
import com.example.kmptemplate.util.getCurrentTime
import com.example.kmptemplate.util.toSystemLocalDateTime
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val sampleRepository: SampleRepository,
    private val feeCategoryRepository: FeeCategoryRepository
) : ViewModel() {
    private val _sampleData = MutableStateFlow(listOf("requesting..."))
    val sampleData: StateFlow<List<String>> = _sampleData

    init {
        KermitLogger.d(TAG) { "init" }
        val localDateTime = getCurrentTime().toSystemLocalDateTime()
        KermitLogger.d(TAG) { "localDateTime = ${localDateTime.dateTimeFormat()}" }
        viewModelScope.launch {
            val result = feeCategoryRepository.getAllCategory()
            when (result) {
                is KmpResult.Failure -> {
                    _sampleData.value = listOf("load failed")
                    KermitLogger.w(TAG) { "load failed error = ${result.error}" }
                }
                is KmpResult.Success -> {
                    val names = result.value.getMostRecentlyUsedList().map { it.name }
                    _sampleData.value = names
                }
            }
        }
    }

    private companion object {
        const val TAG = "MainViewModel"
    }
}
