package com.mobdeve.s18.roman.isaacnathan.alleymate.common.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun SectionHeader(
    title: String,
    modifier: Modifier = Modifier,
    showDivider: Boolean = false,
    isSubtle: Boolean = false
) {
    Column(modifier = modifier) {
        if (showDivider) {
            HorizontalDivider(
                modifier = Modifier.padding(bottom = 12.dp),
                color = Color.LightGray.copy(alpha = 0.5f)
            )
        }

        val textStyle = if (isSubtle) {
            MaterialTheme.typography.bodyMedium
        } else {
            MaterialTheme.typography.titleMedium
        }

        val textColor = if (isSubtle) {
            Color.Gray
        } else {
            MaterialTheme.colorScheme.onSurface
        }

        val textWeight = if (isSubtle) {
            FontWeight.Normal
        } else {
            FontWeight.Bold
        }

        Text(
            text = title,
            style = textStyle,
            color = textColor,
            fontWeight = textWeight
        )
    }
}



@Preview(name = "Standard Header", showBackground = true, widthDp = 300)
@Composable
private fun SectionHeaderPreview() {
    SectionHeader(title = "Current Live Sale")
}

@Preview(name = "Header with Divider", showBackground = true, widthDp = 300)
@Composable
private fun SectionHeaderWithDividerPreview() {
    SectionHeader(title = "5 Total Designs", showDivider = true, isSubtle = true)
}