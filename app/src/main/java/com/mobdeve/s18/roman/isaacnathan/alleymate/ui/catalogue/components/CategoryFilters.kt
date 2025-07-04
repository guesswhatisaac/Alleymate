package com.mobdeve.s18.roman.isaacnathan.alleymate.ui.catalogue.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

// ============================
// Category Filter Chip Row
// ============================

@Composable
fun CategoryFilters(
    categories: List<String>,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit,
    onAddCategoryClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    val selectedColor = Color(0xFFEF6C42)

    // --- Filter Chips Scrollable Row ---
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // --- Category Chips ---
        items(items = categories, key = { it }) { category ->
            val isSelected = selectedCategory == category

            FilterChip(
                selected = isSelected,
                onClick = { onCategorySelected(category) },
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

        // --- Add Category Button ---
        item {
            OutlinedButton(
                onClick = onAddCategoryClicked,
                modifier = Modifier.size(40.dp),
                shape = MaterialTheme.shapes.medium,
                contentPadding = PaddingValues(0.dp),
                border = BorderStroke(1.dp, Color.LightGray)
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Add Category",
                    tint = Color.Gray
                )
            }
        }
    }
}