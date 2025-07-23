package com.mobdeve.s18.roman.isaacnathan.alleymate.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mobdeve.s18.roman.isaacnathan.alleymate.common.components.*
import com.mobdeve.s18.roman.isaacnathan.alleymate.ui.events.components.LiveEventCard
import com.mobdeve.s18.roman.isaacnathan.alleymate.ui.home.components.*

@Composable
fun HomeScreen(
    onNavigateToEvents: () -> Unit,
    onNavigateToLiveSale: (eventId: Int) -> Unit,
    viewModel: HomeViewModel = viewModel(),
    onNavigateToEventDetail: (eventId: Int) -> Unit,
) {
    // --- Observe UI State ---
    val upcomingEvents by viewModel.upcomingEvents.collectAsState()
    val liveEvent by viewModel.liveEvent.collectAsState()
    val overviewStats by viewModel.overviewStats.collectAsState()

    Scaffold(
        topBar = { HomeTopBar(title = "Alleymate") }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // --- Upcoming Events Section ---
            item {
                UpcomingEventsSection(
                    events = upcomingEvents,
                    onViewAllClick = onNavigateToEvents,
                    onEventClick = onNavigateToEventDetail
                )
            }

            // --- Live Sale Section ---
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    SectionHeader(
                        title = "Current Live Sale",
                        showDivider = true,
                        isSubtle = true
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    if (liveEvent != null) {
                        LiveEventCard(
                            event = liveEvent!!,
                            onEventClick = onNavigateToLiveSale
                        )
                    } else {
                        EmptyStateMessage(
                            title = "No Live Sale",
                            subtitle = "Start an event from the Events tab to see it here.",
                            titleColor = Color.Black,
                            subtitleColor = Color.Gray
                        )
                    }
                }
            }

            // --- Performance Snapshot Section ---
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    SectionHeader(
                        title = "Performance Snapshot",
                        showDivider = true,
                        isSubtle = true
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    OverviewGrid(stats = overviewStats)
                }
            }
        }
    }
}
