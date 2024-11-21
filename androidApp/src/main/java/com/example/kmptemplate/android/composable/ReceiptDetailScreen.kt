package com.example.kmptemplate.android.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
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
import com.example.kmptemplate.android.uiState.HeaderState
import com.example.kmptemplate.domainmodel.FeeCategory
import com.example.kmptemplate.domainmodel.Receipt
import com.example.kmptemplate.util.dateTimeFormat
import com.example.kmptemplate.util.toSystemLocalDateTime
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReceiptDetailScreen(
    targetReceipt: Receipt,
    feeCategoryList: List<FeeCategory>,
    headerState: HeaderState,
    onEditReceipt: (Receipt) -> Unit,
    onBack: () -> Unit,
    onDelete: (Receipt) -> Unit,
    modifier: Modifier = Modifier,
) {
    var cost by remember { mutableStateOf(targetReceipt.cost.toString()) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("明細詳細画面") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "戻る"
                        )
                    }
                }
            )
        },
        modifier = modifier
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HeaderPanel(headerState)
            Spacer(modifier = Modifier.height(16.dp))
            CostInputTextField(
                initialInputValue = cost,
                onDone = {
                    cost = it.toString()
                    val editedReceipt = targetReceipt.copy(cost = cost.toInt())
                    onEditReceipt(editedReceipt)
                         },
                labelText = "価格",
            )
            Spacer(modifier = Modifier.height(16.dp))
            SelectCategoryMenu(
                feeCategoryList = feeCategoryList,
                selectedCategory = targetReceipt.category,
                onSelected = {
                    val editedReceipt = targetReceipt.copy(category = it)
                    onEditReceipt(editedReceipt)
                }
            )
            Spacer(modifier = Modifier.height(32.dp))
            val createdText = "作成日: " + targetReceipt.createdAt.toSystemLocalDateTime().dateTimeFormat()
            Text(
                text = createdText
            )
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = { onDelete(targetReceipt) }
            ) {
                Text("削除")
            }
            Spacer(modifier = Modifier.padding(16.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SelectCategoryMenu(
    feeCategoryList: List<FeeCategory>,
    selectedCategory: FeeCategory?,
    onSelected: (FeeCategory?) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    // 表示用の状態
    var selectedCategoryState by remember { mutableStateOf(selectedCategory) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            readOnly = true,
            value = selectedCategory?.name ?: FeeCategory.NoCategoryLabel,
            onValueChange = { },
            label = { Text("カテゴリ") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.menuAnchor(
                type = MenuAnchorType.PrimaryNotEditable,
                enabled = true
            )
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            feeCategoryList.forEach { category ->
                DropdownMenuItem(
                    text = { Text(category.name) },
                    onClick = {
                        selectedCategoryState = category
                        expanded = false
                        onSelected(category)
                    }
                )
            }
            // カテゴリを未入力にする方法を用意
            DropdownMenuItem(
                text = { Text(FeeCategory.NoCategoryLabel) },
                onClick = {
                    selectedCategoryState = null
                    expanded = false
                    onSelected(null)
                }
            )
        }
    }

}

@PreviewLightDark
@Composable
private fun ReceiptDetailScreenPreview() {
    val pseudoInstant = LocalDateTime(
        year = 2023,
        monthNumber = 10,
        dayOfMonth = 10,
        hour = 10,
        minute = 10
    ).toInstant(TimeZone.currentSystemDefault())
    val sampleFeeCategories = listOf(
        FeeCategory("1", "食費", pseudoInstant),
        FeeCategory("2", "交通費", pseudoInstant),
        FeeCategory("3", "通信費", pseudoInstant),
        FeeCategory("4", "光熱費", pseudoInstant),
    )
    MyApplicationTheme {
        Surface {
            ReceiptDetailScreen(
                targetReceipt = Receipt.makeInstanceForPreview(1000, "食費"),
                feeCategoryList = sampleFeeCategories,
                headerState = HeaderState.Normal("requesting..."),
                onEditReceipt = {},
                onBack = {},
                onDelete = {}
            )
        }
    }
}


