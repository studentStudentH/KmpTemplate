package com.example.kmptemplate.android.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.example.kmptemplate.android.MyApplicationTheme
import com.example.kmptemplate.domainmodel.FeeCategory
import com.example.kmptemplate.domainmodel.Receipt
import com.example.kmptemplate.util.dateFormat
import com.example.kmptemplate.util.toSystemLocalDateTime
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant

@Composable
fun ReceiptListItem(
    receipt: Receipt,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    val dateLabelText = receipt.createdAt.toSystemLocalDateTime().dateFormat()
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .clickable { onClick() }
                .padding(horizontal = 8.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            CostLabel(
                cost = receipt.cost,
                costLabelStyle = MaterialTheme.typography.titleMedium,
                unitLabelStyle = MaterialTheme.typography.labelSmall,
            )
            Text(
                text = dateLabelText,
                color = MaterialTheme.colorScheme.outline,
                style = MaterialTheme.typography.labelSmall,
            )
        }
        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = "${receipt.cost}円、${dateLabelText}の詳細へ",
            tint = MaterialTheme.colorScheme.outlineVariant,
        )
    }
}

@PreviewLightDark
@Composable
private fun ReceiptListItemPreview() {
    val pseudoInstant =
        LocalDateTime(
            year = 2024,
            monthNumber = 1,
            dayOfMonth = 1,
            hour = 1,
            minute = 0,
            second = 0,
        ).toInstant(TimeZone.currentSystemDefault())
    val receipt =
        Receipt(
            id = "pseudo_id",
            cost = 1000,
            category =
                FeeCategory(
                    id = "pseudo_id",
                    name = "食費",
                    lastUsedAt = pseudoInstant,
                ),
            createdAt = pseudoInstant,
        )
    MyApplicationTheme {
        Surface {
            ReceiptListItem(receipt)
        }
    }
}
