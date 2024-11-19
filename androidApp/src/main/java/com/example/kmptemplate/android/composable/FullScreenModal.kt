package com.example.kmptemplate.android.composable

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.window.Dialog
import com.example.kmptemplate.android.MyApplicationTheme

@Composable
fun FullScreenModal(
    title: String,
    onDismiss: () -> Unit,
    content: @Composable () -> Unit,
) {
    Dialog(
        onDismissRequest = { onDismiss() }
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = { ModalTopBar(title, onDismiss) }
        ) {
            Box(modifier = Modifier.padding(it)) {
                content()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ModalTopBar(
    title: String = "",
    onCloseClick: () -> Unit
) {
    TopAppBar(
        title = { Text(title) },
        navigationIcon = {
            IconButton(onClick = onCloseClick ) {
                Icon(Icons.Outlined.Close, contentDescription = "Close")
            }
        },
    )
}

@PreviewLightDark
@Composable
private fun FullScreenModalPreview() {
    MyApplicationTheme {
        FullScreenModal(
            title = "full screen modal",
            onDismiss = {}
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("hello")
            }
        }
    }
}
