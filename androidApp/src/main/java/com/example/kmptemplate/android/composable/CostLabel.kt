package com.example.kmptemplate.android.composable

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.example.kmptemplate.android.MyApplicationTheme

@Composable
fun CostLabel(
    cost: Int,
    costLabelStyle: TextStyle,
    unitLabelStyle: TextStyle,
    modifier: Modifier = Modifier,
) {
    Row(modifier) {
        Text(
            text = cost.toString(),
            style = costLabelStyle,
            modifier = Modifier.alignByBaseline(),
        )
        Text(
            text = "円",
            style = unitLabelStyle,
            modifier = Modifier.alignByBaseline(),
        )
    }
}

@PreviewLightDark
@Composable
private fun CostLabelPreview() {
    MyApplicationTheme {
        Surface {
            CostLabel(
                cost = 1000,
                costLabelStyle = MaterialTheme.typography.titleLarge,
                unitLabelStyle = MaterialTheme.typography.labelSmall,
            )
        }
    }
}
