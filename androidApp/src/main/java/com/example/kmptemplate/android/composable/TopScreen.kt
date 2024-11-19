package com.example.kmptemplate.android.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.example.kmptemplate.android.MyApplicationTheme
import com.example.kmptemplate.android.TopScreenInteractions
import com.example.kmptemplate.android.uiState.HeaderState
import com.example.kmptemplate.android.uiState.LoadingState
import com.example.kmptemplate.android.uiState.makeCategorySummary
import com.example.kmptemplate.domainmodel.Receipt
import com.example.kmptemplate.domainmodel.ReceiptCollection
import com.example.kmptemplate.domainmodel.YearMonth

@Composable
fun TopScreen(
    receiptCollection: ReceiptCollection?,
    headerState: HeaderState,
    loadingState: LoadingState,
    startYearMonth: YearMonth,
    endYearMonth: YearMonth?,
    interactions: TopScreenInteractions,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        floatingActionButton =  { MyFloatingActionButton { } }
    ) {
        // 悪いマナーだが、ヘッダーの有無でアイテムの位置が変わると煩わしいので
        // Boxをつかって重なりを許容する形で配置している
        // ToDo: ヘッダーを透明にするなど別のアプローチを取る
        Box(modifier = modifier.padding(it)) {
            HeaderPanel(headerState)
            when(loadingState) {
                LoadingState.Loading -> {
                    MessagePanel(text = "Loading...")
                }
                is LoadingState.LoadFailed -> {
                    MessagePanel(text = loadingState.msg)
                }
                LoadingState.Completed -> {
                    if (receiptCollection == null) {
                        MessagePanel(text = "データがありません")
                        return@Box
                    }
                    TopScreenContent(
                        receiptCollection,
                        startYearMonth,
                        endYearMonth,
                        interactions,
                        modifier.padding(it)
                    )
                }
            }
        }
    }
}

@Composable
private fun MyFloatingActionButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    FloatingActionButton(onClick = onClick, modifier = modifier) {
        Icon(Icons.Filled.Add, "Floating action button.")
    }
}

@Composable
private fun HeaderPanel(
    headerState: HeaderState,
    modifier: Modifier = Modifier
) {
    when(headerState) {
        is HeaderState.Error -> {
            Box(
                modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp)
                    .background(MaterialTheme.colorScheme.error)
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
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = headerState.msg,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                )
            }
        }
    }
}

@Composable
private fun MessagePanel(
    text: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text, style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
private fun TopScreenContent(
    receiptCollection: ReceiptCollection,
    startYearMonth: YearMonth,
    endYearMonth: YearMonth?,
    interactions: TopScreenInteractions,
    modifier: Modifier = Modifier,
) {
    // ToDo: ユーザデータの一番早い時刻を代入するように修正すべき
    val baseYearMonth = YearMonth(year = 2024, month = 1)
    val categorySummaryList = receiptCollection.splitByCategory().map { it.makeCategorySummary() }
    val receiptList = receiptCollection.sortByInstantDescending()
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Spacer(Modifier.height(24.dp)) // ヘッダーと重ならないようにしている
        StatisticsPanel(categorySummaryList)
        EditSpanPanel(
            baseYearMonth = baseYearMonth,
            startYearMonth = startYearMonth,
            endYearMonth = endYearMonth,
            onSelectStartYearMonth = { interactions.onStartYearMonthChanged(it) },
            onSelectEndYearMonth = { interactions.onEndYearMonthChanged(it) }
        )
        HorizontalDivider()
        receiptList.forEach { receipt ->
            ReceiptListItem(
                receipt = receipt,
                onClick = { interactions.onReceiptSelected(receipt) }
            )
            HorizontalDivider()
        }
    }
}

@PreviewLightDark
@Composable
private fun TopScreenPreviewNormal() {
    MyApplicationTheme {
        TopScreen(
            receiptCollection = ReceiptCollection.makeInstanceForPreview(),
            headerState = HeaderState.Error("何かがおかしいです"),
            loadingState = LoadingState.Completed,
            startYearMonth = YearMonth(2024, 1),
            endYearMonth = null,
            interactions = TopScreenInteractionsStub(),
        )
    }
}

@PreviewLightDark
@Composable
private fun TopScreenPreviewLoading() {
    MyApplicationTheme {
        TopScreen(
            receiptCollection = ReceiptCollection.makeInstanceForPreview(),
            headerState = HeaderState.Normal("requesting..."),
            loadingState = LoadingState.Loading,
            startYearMonth = YearMonth(2024, 1),
            endYearMonth = null,
            interactions = TopScreenInteractionsStub(),
        )
    }
}

/**
 * Preview用
 */
private class TopScreenInteractionsStub: TopScreenInteractions {
    override fun onReceiptSelected(receipt: Receipt) {}

    override fun onAddReceipt(cost: Int) {}

    override fun onStartYearMonthChanged(newStartYearMonth: YearMonth) {}

    override fun onEndYearMonthChanged(newEndYearMonth: YearMonth) {}
}
