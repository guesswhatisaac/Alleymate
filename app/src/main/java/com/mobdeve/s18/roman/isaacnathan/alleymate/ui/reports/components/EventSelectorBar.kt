package com.mobdeve.s18.roman.isaacnathan.alleymate.ui.reports.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun EventSelectorBar(
    eventName: String,
    eventDate: String,
    onSelectorClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.tertiary.copy(alpha = 0.1f))
            .clickable { onSelectorClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.DateRange,
            contentDescription = "Select Event",
            tint = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = eventName, style = MaterialTheme.typography.titleLarge)
            Text(text = eventDate, style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
        }
        Icon(
            imageVector = Icons.Default.ArrowDropDown,
            contentDescription = "Dropdown",
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Preview
@Composable
private fun EventSelectorBarPreview() {
    EventSelectorBar(
        eventName = "KOMIKET '25",
        eventDate = "October 25 - October 27",
        onSelectorClick = {}
    )
}