package com.example.kmptemplate.android.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Divider
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.example.kmptemplate.android.MyApplicationTheme
import com.example.kmptemplate.android.uiState.makeCategorySummary
import com.example.kmptemplate.domainmodel.Receipt
import com.example.kmptemplate.domainmodel.ReceiptCollection

@Composable
fun TopScreen(
    receiptCollection: ReceiptCollection,
    onReceiptClick: (Receipt) -> Unit,
    onFloatingActionButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        floatingActionButton =  { MyFloatingActionButton { onFloatingActionButtonClick() } }
    ) {
        TopScreenContent(receiptCollection, modifier.padding(it), onReceiptClick)
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
private fun TopScreenContent(
    receiptCollection: ReceiptCollection,
    modifier: Modifier = Modifier,
    onReceiptClick: (Receipt) -> Unit
) {
    val categorySummaryList = receiptCollection.splitByCategory().map { it.makeCategorySummary() }
    val receiptList = receiptCollection.sortByInstantDecending()
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        StatisticsPanel(categorySummaryList)
        Divider()
        receiptList.forEach { receipt ->
            ReceiptListItem(
                receipt = receipt,
                onClick = { onReceiptClick(receipt) }
            )
            Divider()
        }
    }
}

@PreviewLightDark
@Composable
private fun TopScreenPreview() {
    MyApplicationTheme {
        TopScreen(
            receiptCollection = ReceiptCollection.makeInstanceForPreview(),
            onReceiptClick = {},
            onFloatingActionButtonClick = {}
        )
    }
}
