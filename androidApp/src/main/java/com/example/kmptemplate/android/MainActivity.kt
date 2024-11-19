package com.example.kmptemplate.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.kmptemplate.android.composable.TopScreen
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel by viewModel<MainViewModel>()
        setContent {
            val initialLoadingState by viewModel.initialLoadingState.collectAsState()
            val headerState by viewModel.headerState.collectAsState()
            val receiptCollection by viewModel.receiptCollection.collectAsState()
            val startYearMonth by viewModel.startYearMonth.collectAsState()
            val endYearMonth by viewModel.endYearMonth.collectAsState()
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    TopScreen(
                        receiptCollection = receiptCollection,
                        headerState = headerState,
                        loadingState = initialLoadingState,
                        startYearMonth = startYearMonth,
                        endYearMonth = endYearMonth,
                        interactions = viewModel,
                    )
                    Text("hello")
                }
            }
        }
    }
}
