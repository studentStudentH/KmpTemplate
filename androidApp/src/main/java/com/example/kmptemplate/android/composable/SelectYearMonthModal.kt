package com.example.kmptemplate.android.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.example.kmptemplate.android.MyApplicationTheme
import com.example.kmptemplate.domainmodel.YearMonth

@Composable
fun SelectYearMonthModal(
    title: String,
    baseYearMonth: YearMonth,
    onDismiss: () -> Unit,
    onSelectYearMonth: (YearMonth) -> Unit
) {
    val yearMonthList = baseYearMonth.makeNextYearMonthList(YearMonth.makeCurrentYearMonth())
    FullScreenModal(
        title = title,
        onDismiss = onDismiss
    ) {
        YearMonthList(
            items = yearMonthList,
            onDismiss = onDismiss,
            onSelectYearMonth = onSelectYearMonth
        )
    }
}

@Composable
private fun YearMonthList(
    items: List<YearMonth>,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    onSelectYearMonth: (YearMonth) -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        HorizontalDivider()
        items.forEach {
            TextButton(
                onClick = {
                    onSelectYearMonth(it)
                    onDismiss() // アイテムを選択したら画面を閉じる
                }
            ) {
                Text(it.toLabelString())
            }
            HorizontalDivider()
        }
    }
}

@PreviewLightDark
@Composable
private fun SelectYearMonthModalPreview() {
    val baseYearMonth = YearMonth(2023, 1)
    MyApplicationTheme {
        Surface {
            SelectYearMonthModal(
                title = "test",
                baseYearMonth = baseYearMonth,
                onDismiss = {},
                onSelectYearMonth = {}
            )
        }
    }
}
