package com.example.kmptemplate.android

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kmptemplate.domainmodel.KmpResult
import com.example.kmptemplate.repository.SampleRepository
import com.example.kmptemplate.util.KermitLogger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val sampleRepository: SampleRepository,
) : ViewModel() {
    private val _sampleData = MutableStateFlow(listOf("requesting..."))
    val sampleData: StateFlow<List<String>> = _sampleData

    init {
        KermitLogger.d(TAG) { "init" }
        viewModelScope.launch {
            val result = sampleRepository.getSampleWords()
            when (result) {
                is KmpResult.Failure -> {
                    _sampleData.value = listOf(result.error.msg)
                }
                is KmpResult.Success -> {
                    _sampleData.value = result.value.list
                }
            }
        }
    }

    private companion object {
        const val TAG = "MainViewModel"
    }
}
