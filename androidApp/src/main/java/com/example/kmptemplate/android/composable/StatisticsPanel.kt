package com.example.kmptemplate.android.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.example.kmptemplate.android.MyApplicationTheme
import com.example.kmptemplate.android.uiState.CategorySummary

@Composable
fun StatisticsPanel(
    categorySummaryList: List<CategorySummary>,
    modifier: Modifier = Modifier,
) {
    val totalCost = categorySummaryList.sumOf { it.totalCost }
    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "総出費",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.labelMedium,
        )
        CostLabel(
            cost = totalCost,
            costLabelStyle = MaterialTheme.typography.headlineLarge,
            unitLabelStyle = MaterialTheme.typography.labelSmall,
        )
        categorySummaryList.forEach {
            CategorySummaryItem(categorySummary = it)
        }
    }
}

@Composable
private fun CategorySummaryItem(
    categorySummary: CategorySummary,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier =
            modifier.fillMaxWidth().padding(
                horizontal = 16.dp,
                vertical = 4.dp,
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = categorySummary.categoryName,
            style = MaterialTheme.typography.titleMedium,
        )
        CostLabel(
            cost = categorySummary.totalCost,
            costLabelStyle = MaterialTheme.typography.titleMedium,
            unitLabelStyle = MaterialTheme.typography.labelSmall,
        )
    }
}

@PreviewLightDark
@Composable
private fun StatisticsPanelPreview() {
    MyApplicationTheme {
        Surface {
            StatisticsPanel(
                categorySummaryList =
                    listOf(
                        CategorySummary(totalCost = 1000, categoryName = "食費"),
                        CategorySummary(totalCost = 2000, categoryName = "交通費"),
                        CategorySummary(totalCost = 3000, categoryName = "その他"),
                    ),
            )
        }
    }
}
