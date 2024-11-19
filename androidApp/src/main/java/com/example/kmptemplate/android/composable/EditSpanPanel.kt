package com.example.kmptemplate.android.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.example.kmptemplate.android.MyApplicationTheme
import com.example.kmptemplate.domainmodel.YearMonth

/**
 * baseYearMonthは年月を選択させるリスト表示の範囲を定めるために使う
 */
@Composable
fun EditSpanPanel(
    baseYearMonth: YearMonth,
    startYearMonth: YearMonth,
    endYearMonth: YearMonth?,
    onSelectStartYearMonth: (YearMonth) -> Unit,
    onSelectEndYearMonth: (YearMonth) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        YearMonthTextField(
            textFieldValue = startYearMonth.toLabelString(),
            baseYearMonth = baseYearMonth,
            label = "開始月を入力してください",
            onYearMonthSelected = onSelectStartYearMonth
        )
        YearMonthTextField(
            textFieldValue = endYearMonth?.toLabelString() ?: "",
            baseYearMonth = baseYearMonth,
            label = "終了月を入力してください",
            onYearMonthSelected = onSelectEndYearMonth
        )
    }
}

@Composable
private fun YearMonthTextField(
    textFieldValue: String,
    baseYearMonth: YearMonth,
    label: String,
    modifier: Modifier = Modifier,
    onYearMonthSelected: (YearMonth) -> Unit,
) {
    var showDialog by remember { mutableStateOf(false) }
    OutlinedTextField(
        value = textFieldValue,
        onValueChange = { },
        label = { Text(label) },
        readOnly = true,
        trailingIcon = {
            IconButton(
                onClick = { showDialog = true }
            ) {
                Icon(
                    imageVector = Icons.Default.CalendarMonth,
                    contentDescription = label
                )
            }
        },
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp)
    )
    if (showDialog) {
        SelectYearMonthModal(
            title = label,
            baseYearMonth = baseYearMonth,
            onDismiss = { showDialog = false },
            onSelectYearMonth = onYearMonthSelected
        )
    }
}

@PreviewLightDark
@Composable
fun EditSpanPanelPreview() {
    val baseYearMonth = YearMonth(2023, 1)
    MyApplicationTheme {
        Surface {
            EditSpanPanel(
                baseYearMonth = baseYearMonth,
                startYearMonth = YearMonth.makeCurrentYearMonth(),
                endYearMonth = null,
                onSelectStartYearMonth = {},
                onSelectEndYearMonth = {}
            )
        }
    }
}
