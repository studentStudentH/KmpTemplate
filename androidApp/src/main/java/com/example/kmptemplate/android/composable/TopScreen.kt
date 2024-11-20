package com.example.kmptemplate.android.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
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
    var showAddReceiptModal by remember { mutableStateOf(false) }
    if (showAddReceiptModal) {
        AddReceiptModal(
            initialInputValue = "",
            onDismiss = { showAddReceiptModal = false },
            onAdd = { interactions.onAddReceipt(it) },
        )
    }
    Scaffold(
        floatingActionButton = {
            MyFloatingActionButton { showAddReceiptModal = true }
        },
    ) {
        // 悪いマナーだが、ヘッダーの有無でアイテムの位置が変わると煩わしいので
        // Boxをつかって重なりを許容する形で配置している
        // ToDo: ヘッダーを透明にするなど別のアプローチを取る
        Box(modifier = modifier.padding(it)) {
            HeaderPanel(headerState)
            when (loadingState) {
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
                        modifier.padding(it),
                    )
                }
            }
        }
    }
}

@Composable
private fun MyFloatingActionButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    FloatingActionButton(onClick = onClick, modifier = modifier) {
        Icon(Icons.Filled.Add, "Floating action button.")
    }
}

@Composable
private fun HeaderPanel(
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

@Composable
private fun MessagePanel(
    text: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
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
    val listState = rememberLazyListState()
    // アイテム数が変わった時に最初のアイテムの場所へスクロールする
    LaunchedEffect(receiptList.size) {
        listState.scrollToItem(0)
    }
    Column(
        modifier = modifier.fillMaxWidth(),
    ) {
        Spacer(Modifier.height(32.dp)) // ヘッダーと重ならないようにしている
        StatisticsPanel(categorySummaryList)
        EditSpanPanel(
            baseYearMonth = baseYearMonth,
            startYearMonth = startYearMonth,
            endYearMonth = endYearMonth,
            onSelectStartYearMonth = { interactions.onStartYearMonthChanged(it) },
            onSelectEndYearMonth = { interactions.onEndYearMonthChanged(it) },
        )
        HorizontalDivider()
        LazyColumn(state = listState) {
            items(receiptList, key = { it.id }) { receipt ->
                ReceiptListItem(
                    modifier = Modifier.animateItem(),
                    receipt = receipt,
                    onClick = { interactions.onReceiptSelected(receipt) },
                )
                HorizontalDivider()
            }
        }
        // データが空の時の表示
        if (receiptList.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                val text =
                    if (endYearMonth == null) {
                        "右下のボタンから明細を追加しましょう！"
                    } else {
                        "検索条件を満たすデータは0件です"
                    }
                Text(
                    text = text,
                    style = MaterialTheme.typography.bodyMedium,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.align(Alignment.Center),
                )
            }
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

@PreviewLightDark
@Composable
private fun TopScreenPreviewNoData() {
    MyApplicationTheme {
        TopScreen(
            receiptCollection = ReceiptCollection(listOf()),
            headerState = HeaderState.Normal("requesting..."),
            loadingState = LoadingState.Completed,
            startYearMonth = YearMonth(2024, 1),
            endYearMonth = null,
            interactions = TopScreenInteractionsStub(),
        )
    }
}

/**
 * Preview用
 */
private class TopScreenInteractionsStub : TopScreenInteractions {
    override fun onReceiptSelected(receipt: Receipt) {}

    override fun onAddReceipt(cost: Int) {}

    override fun onStartYearMonthChanged(newStartYearMonth: YearMonth) {}

    override fun onEndYearMonthChanged(newEndYearMonth: YearMonth) {}
}
