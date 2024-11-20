package com.example.kmptemplate.android.uiState

sealed interface HeaderState {
    data object None : HeaderState

    data class Normal(val msg: String) : HeaderState

    data class Error(val msg: String) : HeaderState
}
