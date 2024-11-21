package com.example.kmptemplate.android.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.example.kmptemplate.android.MyApplicationTheme

@Composable
fun AddReceiptModal(
    initialInputValue: String,
    onDismiss: () -> Unit,
    onAdd: (receiptCost: Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    FullScreenModal(
        title = "値段を入力してください",
        onDismiss = onDismiss,
    ) {
        Column(
            modifier = modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.fillMaxWidth().height(16.dp))
            // Enterを押したらonAddが呼び出されるようにする
            CostInputTextField(
                initialInputValue = initialInputValue,
                onDone = onAdd,
                onDismiss = onDismiss,
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun AddReceiptModalPreviewWithNumber() {
    MyApplicationTheme {
        AddReceiptModal(
            initialInputValue = "10",
            onDismiss = {},
            onAdd = {},
        )
    }
}

@PreviewLightDark
@Composable
private fun AddReceiptModalPreviewWithInvalidNumber() {
    MyApplicationTheme {
        AddReceiptModal(
            initialInputValue = "-10",
            onDismiss = {},
            onAdd = {},
        )
    }
}

@PreviewLightDark
@Composable
private fun AddReceiptModalPreviewWithNotNumber() {
    MyApplicationTheme {
        AddReceiptModal(
            initialInputValue = "hello",
            onDismiss = {},
            onAdd = {},
        )
    }
}
