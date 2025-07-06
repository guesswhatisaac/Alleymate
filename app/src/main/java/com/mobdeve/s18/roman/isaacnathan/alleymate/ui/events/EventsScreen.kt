    package com.mobdeve.s18.roman.isaacnathan.alleymate.ui.events

    import androidx.compose.foundation.layout.*
    import androidx.compose.foundation.lazy.LazyColumn
    import androidx.compose.foundation.lazy.items
    import androidx.compose.material3.*
    import androidx.compose.runtime.*
    import androidx.compose.ui.Modifier
    import androidx.compose.ui.unit.dp
    import com.mobdeve.s18.roman.isaacnathan.alleymate.common.components.AppFloatingActionButton
    import com.mobdeve.s18.roman.isaacnathan.alleymate.common.components.AppTopBar
    import com.mobdeve.s18.roman.isaacnathan.alleymate.common.components.SectionHeader
    import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.CatalogueItem
    import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.Event
    import com.mobdeve.s18.roman.isaacnathan.alleymate.ui.events.components.AddEventModal
    import com.mobdeve.s18.roman.isaacnathan.alleymate.ui.events.components.EventInventoryModal
    import com.mobdeve.s18.roman.isaacnathan.alleymate.ui.events.components.EventListItem
    import com.mobdeve.s18.roman.isaacnathan.alleymate.ui.events.components.ExpensesTrackerModal
    import com.mobdeve.s18.roman.isaacnathan.alleymate.ui.events.components.EditEventModal
    import androidx.lifecycle.viewmodel.compose.viewModel

    private sealed interface EventModalState {
        data object None : EventModalState
        data object AddEvent : EventModalState
        data class Inventory(val event: Event) : EventModalState
        data class Expenses(val event: Event) : EventModalState
        data class EditEvent(val event: Event) : EventModalState
    }

    @Composable
    fun EventsScreen(
        onNavigateToEventDetail: (Int) -> Unit,
        viewModel: EventViewModel = viewModel()
    ) {

        var modalState by remember { mutableStateOf<EventModalState>(EventModalState.None) }
        val events by viewModel.allEvents.collectAsState()

        when (val state = modalState) {
            is EventModalState.None -> { /* Show nothing */ }
            is EventModalState.AddEvent -> {
                AddEventModal(
                    onDismissRequest = { modalState = EventModalState.None },
                    onAddEvent = { title, location, startDate, endDate ->
                        viewModel.addEvent(title, location, startDate, endDate)
                        modalState = EventModalState.None
                    }
                )
            }
            is EventModalState.Inventory -> {
                val inventoryItems = listOf(
                    CatalogueItem(1, "MHYLOW star sticker", "Sticker", 100.0, 50),
                    CatalogueItem(2, "Art Print A", "Print", 250.0, 20)
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

                items(items = events, key = { it.eventId }) { event ->
                    EventListItem(
                        event = event,
                        onEventClick = {
                            onNavigateToEventDetail(event.eventId)
                        },
                        onEditClick = { modalState = EventModalState.EditEvent(event) }
                    )
                }

            }
        }
    }
