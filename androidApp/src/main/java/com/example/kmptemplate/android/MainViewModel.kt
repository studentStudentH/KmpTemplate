package com.example.kmptemplate.android

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kmptemplate.repository.SampleRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val sampleRepository: SampleRepository
): ViewModel() {
    private val _sampleData = MutableStateFlow(listOf("requesting..."))
    val sampleData: StateFlow<List<String>> = _sampleData

    init {
        viewModelScope.launch {
            val result = sampleRepository.getSampleSentences()
            result.onSuccess { listHolder ->
                _sampleData.value = listHolder.list
            }
            result.onFailure { exception ->
                val errorMsg = exception.message ?: "no error msg"
                _sampleData.value = listOf(errorMsg)
            }
        }
    }
}
