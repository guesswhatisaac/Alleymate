package com.mobdeve.s18.roman.isaacnathan.alleymate.ui.reports.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun TimeFilterChips(modifier: Modifier = Modifier) {
    var selectedTime by remember { mutableStateOf("ALL TIME") }

    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        item {
            FilterChip(
                selected = true, // Always selected for this design
                onClick = { /* TODO: Handle other time filters */ },
                label = { Text("ALL TIME") },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    selectedLabelColor = MaterialTheme.colorScheme.onSurface
                ),
                border = FilterChipDefaults.filterChipBorder(
                    borderColor = MaterialTheme.colorScheme.outlineVariant,
                    selected = false,
                    enabled = false,
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