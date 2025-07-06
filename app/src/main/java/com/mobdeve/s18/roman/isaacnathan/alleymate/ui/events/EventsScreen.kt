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
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.Event
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.EventStatus
import com.mobdeve.s18.roman.isaacnathan.alleymate.ui.events.components.AddEventModal
import com.mobdeve.s18.roman.isaacnathan.alleymate.ui.events.components.EventListItem
import com.mobdeve.s18.roman.isaacnathan.alleymate.ui.events.components.EditEventModal
import com.mobdeve.s18.roman.isaacnathan.alleymate.ui.events.components.LiveEventCard
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mobdeve.s18.roman.isaacnathan.alleymate.common.components.EmptyStateMessage

private sealed interface EventModalState {
    data object None : EventModalState
    data object AddEvent : EventModalState
    data class EditEvent(val event: Event) : EventModalState
}

private enum class EventTab(val title: String) {
    UPCOMING("Upcoming"),
    PAST("Past")
}

@Composable
fun EventsScreen(
    onNavigateToEventDetail: (Int) -> Unit,
    viewModel: EventViewModel = viewModel()
) {
    var modalState by remember { mutableStateOf<EventModalState>(EventModalState.None) }
    var selectedTab by remember { mutableStateOf(EventTab.UPCOMING) }
    val events by viewModel.allEvents.collectAsState()

    // Filter events by status
    val liveEvents = events.filter { it.status == EventStatus.LIVE }
    val upcomingEvents = events.filter { it.status == EventStatus.UPCOMING }
    val pastEvents = events.filter { it.status == EventStatus.ENDED }

    // Modal handling
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
        is EventModalState.EditEvent -> {
            EditEventModal(
                event = state.event,
                onDismissRequest = { modalState = EventModalState.None },
                onConfirmEdit = { updatedEvent ->
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Live Event Section (always show)
            Column(
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                SectionHeader(
                    title = "Currently Live",
                    showDivider = false,
                    isSubtle = false
                )
                Spacer(modifier = Modifier.height(8.dp))

                if (liveEvents.isNotEmpty()) {
                    // Show the first live event prominently
                    LiveEventCard(
                        event = liveEvents.first(),
                        onEventClick = { onNavigateToEventDetail(liveEvents.first().eventId) },
                        //onEditClick = { modalState = EventModalState.EditEvent(liveEvents.first()) }
                    )
                } else {
                    // Show empty state for live events
                    EmptyStateMessage(
                        title = "No Live Events",
                        subtitle = "Start an event to see it here",
                        modifier = Modifier.height(120.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            }

            // Tabs Section
            Column {
                TabRow(
                    selectedTabIndex = selectedTab.ordinal,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    EventTab.entries.forEach { tab ->
                        Tab(
                            selected = selectedTab == tab,
                            onClick = { selectedTab = tab },
                            text = {
                                val count = when (tab) {
                                    EventTab.UPCOMING -> upcomingEvents.size
                                    EventTab.PAST -> pastEvents.size
                                }
                                Text("${tab.title} ($count)")
                            }
                        )
                    }
                }

                // Tab Content
                when (selectedTab) {
                    EventTab.UPCOMING -> {
                        EventList(
                            events = upcomingEvents,
                            onEventClick = onNavigateToEventDetail,
                            onEditClick = { event -> modalState = EventModalState.EditEvent(event) },
                            isPastEvents = false
                        )
                    }
                    EventTab.PAST -> {
                        EventList(
                            events = pastEvents,
                            onEventClick = onNavigateToEventDetail,
                            onEditClick = { event -> modalState = EventModalState.EditEvent(event) },
                            isPastEvents = true
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun EventList(
    events: List<Event>,
    onEventClick: (Int) -> Unit,
    onEditClick: (Event) -> Unit,
    isPastEvents: Boolean
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (events.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        contentAlignment = androidx.compose.ui.Alignment.Center
                    ) {
                        Text(
                            text = if (isPastEvents) "No past events" else "No upcoming events",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        } else {
            items(items = events, key = { it.eventId }) { event ->
                EventListItem(
                    event = event,
                    onEventClick = { onEventClick(event.eventId) },
                    //onEditClick = { onEditClick(event) },
                )
            }
        }
    }
}
