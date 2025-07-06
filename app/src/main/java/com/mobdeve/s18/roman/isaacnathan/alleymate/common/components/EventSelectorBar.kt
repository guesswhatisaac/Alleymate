package com.mobdeve.s18.roman.isaacnathan.alleymate.common.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.Event

@Composable
fun EventSelectorBar(
    currentEvent: Event?,
    events: List<Event>,
    onEventSelected: (Event) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .clickable { expanded = true }
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
            if (currentEvent != null) {
                Text(text = currentEvent.title, style = MaterialTheme.typography.titleLarge)
                Text(text = currentEvent.dateRangeString, style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
            } else {
                Text(text = "Select an Event", style = MaterialTheme.typography.titleLarge)
                Text(text = "Tap to choose from list", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
            }
        }
        Box {
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = "Dropdown",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.rotate(if (expanded) 180f else 0f)
            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .width(250.dp)
                    .background(MaterialTheme.colorScheme.surface),
                properties = PopupProperties(focusable = true)
            ) {
                if (events.isEmpty()) {
                    DropdownMenuItem(
                        text = { Text("No events created yet.") },
                        onClick = { expanded = false },
                        enabled = false
                    )
                } else {
                    events.forEach { event ->
                        EventDropdownItem(
                            event = event,
                            onClick = {
                                onEventSelected(event)
                                expanded = false
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun EventDropdownItem(
    event: Event,
    onClick: () -> Unit
) {
    DropdownMenuItem(
        text = {
            Column {
                Text(
                    text = event.title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = event.dateRangeString,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
        },
        onClick = onClick,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    )
}