package com.mobdeve.s18.roman.isaacnathan.alleymate.ui.events

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mobdeve.s18.roman.isaacnathan.alleymate.common.components.AppFloatingActionButton
import com.mobdeve.s18.roman.isaacnathan.alleymate.common.components.AppTopBar
import com.mobdeve.s18.roman.isaacnathan.alleymate.common.components.SectionHeader
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.Event
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.EventStatus
import com.mobdeve.s18.roman.isaacnathan.alleymate.ui.events.components.AddEventModal
import com.mobdeve.s18.roman.isaacnathan.alleymate.ui.events.components.EventListItem

@Composable
fun EventsScreen() {

    val events = listOf(
        Event(
            id = 1,
            title = "KOMIKET ‘25",
            date = "Oct. 25 - Oct. 26, 2025",
            location = "SM Megamall B | Table 35",
            status = EventStatus.LIVE,
            itemsSold = 0,
            itemsAllocated = 0,
            expenses = 0,
            profit = 0
        ),
        Event(
            id = 2,
            title = "STATION '25",
            date = "Nov. 15 - Nov. 16, 2025",
            location = "Whitespace Manila",
            status = EventStatus.UPCOMING,
            itemsSold = 0,
            itemsAllocated = 0,
            expenses = 0,
            profit = 0
        ),
        Event(
            id = 3,
            title = "COSMANIA '24",
            date = "Dec. 7 - Dec. 8, 2024",
            location = "SMX Convention Center",
            status = EventStatus.ENDED,
            itemsSold = 0,
            itemsAllocated = 0,
            expenses = 0,
            profit = 0
        )
    )

    var showAddEventModal by remember { mutableStateOf(false) }

    if (showAddEventModal) {
        AddEventModal(
            onDismissRequest = { showAddEventModal = false },
            onAddEvent = {
                // TODO: Handle adding the event logic
                showAddEventModal = false
            }
        )
    }


    Scaffold(
        topBar = {
            AppTopBar(title = "Events")
        },
        floatingActionButton = {
            AppFloatingActionButton(
                onClick = { showAddEventModal = true }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Section header
            item {
                SectionHeader(
                    title = "${events.size} Events",
                    showDivider = true,
                    isSubtle = true
                )
            }

            // Render each event
            items(events) { event ->
                EventListItem(event = event)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun EventsScreenPreview() {
    EventsScreen()
}
