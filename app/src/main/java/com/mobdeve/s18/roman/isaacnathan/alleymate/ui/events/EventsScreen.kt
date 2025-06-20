package com.mobdeve.s18.roman.isaacnathan.alleymate.ui.events

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mobdeve.s18.roman.isaacnathan.alleymate.common.components.AppFloatingActionButton
import com.mobdeve.s18.roman.isaacnathan.alleymate.common.components.AppTopBar
import com.mobdeve.s18.roman.isaacnathan.alleymate.common.components.SectionHeader
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.CatalogueItem
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.Event
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.EventStatus
import com.mobdeve.s18.roman.isaacnathan.alleymate.theme.AlleyMateTheme
import com.mobdeve.s18.roman.isaacnathan.alleymate.ui.events.components.AddEventModal
import com.mobdeve.s18.roman.isaacnathan.alleymate.ui.events.components.EventInventoryModal
import com.mobdeve.s18.roman.isaacnathan.alleymate.ui.events.components.EventListItem
import com.mobdeve.s18.roman.isaacnathan.alleymate.ui.events.components.ExpensesTrackerModal
import com.mobdeve.s18.roman.isaacnathan.alleymate.ui.events.components.EditEventModal


// 1. Define the possible modal states for this screen
private sealed interface EventModalState {
    data object None : EventModalState
    data object AddEvent : EventModalState
    data class Inventory(val event: Event) : EventModalState
    data class Expenses(val event: Event) : EventModalState
    data class EditEvent(val event: Event) : EventModalState
}

@Composable
fun EventsScreen() {
    var modalState by remember { mutableStateOf<EventModalState>(EventModalState.None) }

    val events = listOf(
        Event(1, "KOMIKET ‘25", "Oct. 25-26, 2025", "SM Megamall B | Table 35", EventStatus.LIVE, 200, 150, 5000, 10000),
        Event(2, "STATION '25", "Nov. 15-16, 2025", "Whitespace Manila", EventStatus.UPCOMING, 100, 0, 0, 0),
        Event(3, "COSMANIA '24", "Dec. 7-8, 2024", "SMX Convention Center", EventStatus.ENDED, 300, 300, 8000, 15000)
    )

    when (val state = modalState) {
        is EventModalState.None -> { /* Show nothing */ }
        is EventModalState.AddEvent -> {
            AddEventModal(
                onDismissRequest = { modalState = EventModalState.None },
                onAddEvent = {
                    // TODO: Logic to add a new event
                    modalState = EventModalState.None
                }
            )
        }
        is EventModalState.Inventory -> {
            val inventoryItems = listOf(
                CatalogueItem(1, "MHYLOW star sticker", "Sticker", 100, 50),
                CatalogueItem(2, "Art Print A", "Print", 250, 20)
            )
            EventInventoryModal(
                eventName = state.event.title,
                inventory = inventoryItems,
                onDismissRequest = { modalState = EventModalState.None },
                onRemoveItem = { item ->
                    // TODO: Logic to remove item from event inventory
                    println("Removing ${item.name} from ${state.event.title}")
                }
            )
        }
        is EventModalState.Expenses -> {
            ExpensesTrackerModal(
                eventName = state.event.title,
                onDismissRequest = { modalState = EventModalState.None },
                onAddExpense = { description, amount ->
                    // TODO: Logic to add an expense
                    println("Adding expense to ${state.event.title}: $description - $amount")
                    modalState = EventModalState.None
                }
            )
        }
        is EventModalState.EditEvent -> {
            EditEventModal(
                event = state.event,
                onDismissRequest = { modalState = EventModalState.None },
                onConfirmEdit = { updatedEvent ->
                    // TODO: Handle the event edit logic
                    println("Saving changes for event: $updatedEvent")
                    modalState = EventModalState.None
                }
            )
        }
    }

    Scaffold(
        topBar = {
            AppTopBar(title = "Events")
        },
        floatingActionButton = {
            AppFloatingActionButton(
                onClick = { modalState = EventModalState.AddEvent }
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
            item {
                SectionHeader(
                    title = "${events.size} Events",
                    showDivider = true,
                    isSubtle = true
                )
            }

            items(events) { event ->
                EventListItem(
                    event = event,
                    onInventoryClick = { modalState = EventModalState.Inventory(event) },
                    onExpensesClick = { modalState = EventModalState.Expenses(event) },
                    onEditClick = { modalState = EventModalState.EditEvent(event) }

                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun EventsScreenPreview() {
    AlleyMateTheme {
        EventsScreen()
    }
}