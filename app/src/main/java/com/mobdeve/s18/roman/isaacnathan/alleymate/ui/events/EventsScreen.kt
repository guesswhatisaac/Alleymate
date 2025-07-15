package com.mobdeve.s18.roman.isaacnathan.alleymate.ui.events

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mobdeve.s18.roman.isaacnathan.alleymate.common.components.AppFloatingActionButton
import com.mobdeve.s18.roman.isaacnathan.alleymate.common.components.AppTopBar
import com.mobdeve.s18.roman.isaacnathan.alleymate.common.components.SectionHeader
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.EventStatus
import com.mobdeve.s18.roman.isaacnathan.alleymate.ui.events.components.AddEventModal
import com.mobdeve.s18.roman.isaacnathan.alleymate.ui.events.components.EventListItem
import com.mobdeve.s18.roman.isaacnathan.alleymate.ui.events.components.EditEventModal
import com.mobdeve.s18.roman.isaacnathan.alleymate.ui.events.components.LiveEventCard
import com.mobdeve.s18.roman.isaacnathan.alleymate.common.components.EmptyStateMessage

private sealed interface EventModalState {
    data object None : EventModalState
    data object AddEvent : EventModalState
    data class EditEvent(val event: EventUiModel) : EventModalState
}

private enum class EventTab(val title: String) {
    UPCOMING("Upcoming"),
    PAST("Past")
}

@Composable
fun EventsScreen(
    onNavigateToEventDetail: (Int) -> Unit,
    onNavigateToLiveSale: (eventId: Int) -> Unit,
    viewModel: EventViewModel
    ) {

    var modalState by remember { mutableStateOf<EventModalState>(EventModalState.None) }
    var selectedTab by remember { mutableStateOf(EventTab.UPCOMING) }

    val events by viewModel.allEvents.collectAsState()

    val (liveEvents, upcomingEvents, pastEvents) = remember(events) {
        val live = events.filter { it.status == EventStatus.LIVE }
        val upcoming = events.filter { it.status == EventStatus.UPCOMING }
        val past = events.filter { it.status == EventStatus.ENDED }
        Triple(live, upcoming, past)
    }

    // Modal handling
    when (val state = modalState) {
        is EventModalState.None -> { }
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
                    viewModel.updateEvent(updatedEvent)
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
            // Live Event Section
            Column(
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                SectionHeader(
                    title = "Currently Live",
                    showDivider = true,
                    isSubtle = true
                )
                Spacer(modifier = Modifier.height(8.dp))

                if (liveEvents.isNotEmpty()) {
                    LiveEventCard(
                        event = liveEvents.first(),
                        onEventClick = { eventId ->
                            onNavigateToLiveSale(eventId)
                        },
                    )
                } else {
                    EmptyStateMessage(
                        title = "No Live Events",
                        subtitle = "Start an event to see it here",
                        modifier = Modifier.height(120.dp),
                        titleColor = Color.Black,
                        subtitleColor = Color.Gray
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            }

            // Tabs Section
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
                            isPastEvents = false
                        )
                    }
                    EventTab.PAST -> {
                        EventList(
                            events = pastEvents,
                            onEventClick = onNavigateToEventDetail,
                            isPastEvents = true
                        )
                    }
                }

        }
    }
}

@Composable
private fun EventList(
    events: List<EventUiModel>,
    onEventClick: (Int) -> Unit,
    isPastEvents: Boolean
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (events.isEmpty()) {
            item {
                val title = if (isPastEvents) "No Past Events" else "No Upcoming Events"
                val subtitle = if (isPastEvents) "Completed events will appear here." else "Tap the '+' button to create a new event."
                EmptyStateMessage(
                    title = title,
                    subtitle = subtitle,
                    titleColor = Color.Black,
                    subtitleColor = Color.Gray
                )
            }
        } else {
            items(items = events, key = { it.eventId }) { event ->
                EventListItem(
                    event = event,
                    onEventClick = { onEventClick(event.eventId) },
                )
            }
        }
    }
}