package com.example.kmptemplate.android.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.example.kmptemplate.android.MyApplicationTheme
import com.example.kmptemplate.domainmodel.YearMonth

@Composable
fun SelectYearMonthModal(
    title: String,
    baseYearMonth: YearMonth,
    onDismiss: () -> Unit,
    onSelectYearMonth: (YearMonth) -> Unit,
) {
    val yearMonthList = baseYearMonth.makeNextYearMonthList(YearMonth.makeCurrentYearMonth())
    FullScreenModal(
        title = title,
        onDismiss = onDismiss,
    ) {
        YearMonthList(
            items = yearMonthList,
            onDismiss = onDismiss,
            onSelectYearMonth = onSelectYearMonth,
        )
    }
}

@Composable
private fun YearMonthList(
    items: List<YearMonth>,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    onSelectYearMonth: (YearMonth) -> Unit,
) {
    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        HorizontalDivider()
        items.forEach {
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .clickable {
                            onSelectYearMonth(it)
                            onDismiss() // アイテムを選択したら画面を閉じる
                        }
                        .padding(horizontal = 16.dp, vertical = 16.dp),
            ) {
                Text(
                    text = it.toLabelString(),
                    style = MaterialTheme.typography.titleMedium,
                )
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
                onSelectYearMonth = {},
            )
        }
    }
}
