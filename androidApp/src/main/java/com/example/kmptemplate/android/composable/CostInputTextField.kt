package com.example.kmptemplate.android.composable

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.example.kmptemplate.android.MyApplicationTheme

@Composable
fun CostInputTextField(
    initialInputValue: String,
    labelText: String,
    onDone: (cost: Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    var inputText by remember { mutableStateOf(initialInputValue) }
    OutlinedTextField(
        value = inputText,
        onValueChange = {
            inputText = it
        },
        modifier = modifier,
        label = { Text(labelText) },
        isError = !isValidNumber(inputText),
        supportingText = {
            Text(
                text = makeSupportingText(inputText),
                color = MaterialTheme.colorScheme.error,
            )
        },
        singleLine = true,
        maxLines = 1,
        keyboardOptions =
            KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = androidx.compose.ui.text.input.ImeAction.Done,
            ),
        keyboardActions =
            KeyboardActions(
                onDone = {
                    if (isValidNumber(inputText)) {
                        onDone(inputText.toInt())
                    }
                },
            ),
    )
}

private fun isValidNumber(number: String): Boolean {
    val intNumber = number.toIntOrNull() ?: return false
    return intNumber > 0
}

private fun makeSupportingText(number: String): String {
    val intNumber = number.toIntOrNull() ?: return "数字を入力してください"
    if (intNumber < 0) {
        return "0円以上を入力してください"
    }
    return ""
}

@PreviewLightDark
@Composable
private fun CostInputTextFieldPreviewWithNumber() {
    MyApplicationTheme {
        Surface {
            CostInputTextField(
                initialInputValue = "10",
                labelText = "値段",
                onDone = {},
            )
        }
    }
}
