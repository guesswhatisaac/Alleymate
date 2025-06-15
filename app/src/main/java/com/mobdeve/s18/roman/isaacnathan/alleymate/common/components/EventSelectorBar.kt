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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.Event
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.EventStatus
import com.mobdeve.s18.roman.isaacnathan.alleymate.theme.AlleyMateTheme

@Composable
fun EventSelectorBar(
    currentEventName: String,
    currentEventDate: String,
    events: List<Event>,
    onEventSelected: (Event) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    // This is the main visible bar. Clicking it will also expand the dropdown.
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant) // Use a subtle theme color
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
            Text(text = currentEventName, style = MaterialTheme.typography.titleLarge)
            Text(text = currentEventDate, style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
        }

        // --- 1. WRAP THE ICON IN A BOX TO USE AS AN ANCHOR ---
        Box {
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = "Dropdown",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.rotate(if (expanded) 180f else 0f)
            )

            // --- 2. THE STYLED DROPDOWN MENU ---
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .width(250.dp) // Set a reasonable width for the menu
                    .background(MaterialTheme.colorScheme.surface),
                // This allows the menu to be scrollable if there are many events
                properties = PopupProperties(focusable = true)
            ) {
                events.forEach { event ->
                    // Use our custom item composable
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

// --- 3. A DEDICATED COMPOSABLE FOR THE DROPDOWN ITEMS ---
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
                    style = MaterialTheme.typography.bodyLarge, // A good size for menu items
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = event.date,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
        },
        onClick = onClick,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    )
}


@Preview(showBackground = true)
@Composable
private fun EventSelectorBarPreview() {
    AlleyMateTheme {
        val sampleEvents = listOf(
            Event(1, "KOMIKET ‘25", "Oct 25-27, 2025", "", EventStatus.LIVE, 0, 0, 0, 0),
            Event(2, "Sticker Con '25", "Nov 16, 2025", "", EventStatus.UPCOMING, 0, 0, 0, 0),
            Event(3, "Cosmania '24", "Dec 7-8, 2024", "", EventStatus.ENDED, 0, 0, 0, 0),
        )
        var selectedEvent by remember { mutableStateOf(sampleEvents[0]) }

        EventSelectorBar(
            currentEventName = selectedEvent.title,
            currentEventDate = selectedEvent.date,
            events = sampleEvents,
            onEventSelected = { selectedEvent = it }
        )
    }
}