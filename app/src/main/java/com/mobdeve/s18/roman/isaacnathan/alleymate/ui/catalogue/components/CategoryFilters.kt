package com.mobdeve.s18.roman.isaacnathan.alleymate.ui.catalogue.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun CategoryFilters() {
    val categories = listOf("ALL", "STICKERS", "PRINTS", "JEWELLRY")
    var selectedCategory by remember { mutableStateOf("ALL") }
    val selectedColor = Color(0xFFEF6C42)

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        items(categories) { category ->
            val isSelected = selectedCategory == category

            FilterChip(
                selected = isSelected,
                onClick = { selectedCategory = category },
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
                    enabled = false,
                    selected = false
                )
            )
        }

        item {
            OutlinedButton(
                onClick = { /* TODO: Handle Add Category */ },
                modifier = Modifier.size(40.dp),
                shape = MaterialTheme.shapes.medium,
                contentPadding = PaddingValues(0.dp),
                border = BorderStroke(1.dp, Color.LightGray)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Category", tint = Color.Gray)
            }
        }
    }
}

@Preview
@Composable
private fun CategoryFiltersPreview() {
    CategoryFilters()
}