package com.mobdeve.s18.roman.isaacnathan.alleymate.ui.reports.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun TimeFilterChips(modifier: Modifier = Modifier) {
    val categories = listOf("ALL TIME", "LAST 5", "FIRST 5")
    var selectedTime by remember { mutableStateOf("ALL TIME") }
    val selectedColor = Color(0xFFEF6C42)

    // --- Filter Chips Scrollable Row ---
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // --- Category Chips ---
        items(categories) { category ->
            val isSelected = selectedTime == category

            FilterChip(
                selected = isSelected,
                onClick = { selectedTime = category },
                label = {
                    Text(
                        text = category,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Normal
                    )
                },
                shape = MaterialTheme.shapes.medium,
                colors = FilterChipDefaults.filterChipColors(
                    containerColor = Color.Transparent,
                    labelColor = Color.DarkGray,
                    selectedContainerColor = selectedColor,
                    selectedLabelColor = Color.White
                ),
                border = FilterChipDefaults.filterChipBorder(
                    borderColor = Color.LightGray,
                    selectedBorderColor = Color.Transparent,
                    borderWidth = 1.dp,
                    enabled = true,
                    selected = false
                )
            )
        }
    }
}



@Preview
@Composable
private fun TimeFilterChipsPreview() {
    TimeFilterChips()
}