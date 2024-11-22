package com.example.kmptemplate.android.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
        title = "価格を入力してください",
        onDismiss = onDismiss,
    ) {
        Column(
            modifier = modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(16.dp),
            )
            // Enterを押したらonAddが呼び出されるようにする
            CostInputTextField(
                initialInputValue = initialInputValue,
                labelText = "価格",
                onDone = {
                    onAdd(it)
                    onDismiss() // 画面閉じる
                },
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
