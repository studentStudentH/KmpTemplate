package com.example.kmptemplate.android.uiState

sealed interface LoadingState {
    data object Loading: LoadingState
    data class LoadFailed(val msg: String): LoadingState
    data object Completed: LoadingState
}
