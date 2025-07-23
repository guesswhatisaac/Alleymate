package com.mobdeve.s18.roman.isaacnathan.alleymate.ui.catalogue.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun CategoryFilters(
    categories: List<String>,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit,
    onAddCategoryClicked: () -> Unit,
    onCategoryLongPress: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Render a chip for each category
        items(items = categories, key = { it }) { category ->
            val isSelected = selectedCategory == category

            CustomFilterChip(
                text = category,
                isSelected = isSelected,
                onClick = { onCategorySelected(category) },
                // Prevent long press for special categories
                onLongClick = if (category != "ALL" && category != "N/A") {
                    { onCategoryLongPress(category) }
                } else {
                    null
                }
            )
        }

        // Button for adding a new category
        item {
            OutlinedButton(
                onClick = onAddCategoryClicked,
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

@Composable
private fun CustomFilterChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    onLongClick: (() -> Unit)? = null
) {
    val selectedColor = Color(0xFFEF6C42)
    val backgroundColor = if (isSelected) selectedColor else Color.Transparent
    val textColor = if (isSelected) Color.White else Color.DarkGray
    val border = if (!isSelected) BorderStroke(1.dp, Color.LightGray) else null

    Surface(
        shape = MaterialTheme.shapes.medium,
        color = backgroundColor,
        border = border,
        modifier = Modifier
            .clip(MaterialTheme.shapes.medium)
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick
            )
    ) {
        // Category label
        Text(
            text = text,
            color = textColor,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Normal,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}
