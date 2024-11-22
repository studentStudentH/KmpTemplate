package com.example.kmptemplate.android.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.kmptemplate.android.uiState.HeaderState

@Composable
fun HeaderPanel(
    headerState: HeaderState,
    modifier: Modifier = Modifier,
) {
    when (headerState) {
        is HeaderState.Error -> {
            Box(
                modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp)
                    .background(MaterialTheme.colorScheme.error),
            ) {
                Text(
                    text = headerState.msg,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onError,
                )
            }
        }
        HeaderState.None -> {}
        is HeaderState.Normal -> {
            Box(
                modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(horizontal = 8.dp, vertical = 8.dp),
            ) {
                Text(
                    text = headerState.msg,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onPrimary,
                )
            }
        }
    }
}
