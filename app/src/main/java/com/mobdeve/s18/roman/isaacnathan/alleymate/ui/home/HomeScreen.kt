package com.mobdeve.s18.roman.isaacnathan.alleymate.ui.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mobdeve.s18.roman.isaacnathan.alleymate.common.components.HomeTopBar
import com.mobdeve.s18.roman.isaacnathan.alleymate.common.components.SectionHeader
import com.mobdeve.s18.roman.isaacnathan.alleymate.ui.home.components.OverviewGrid
import com.mobdeve.s18.roman.isaacnathan.alleymate.ui.home.components.UpcomingEventsSection
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.*
import com.mobdeve.s18.roman.isaacnathan.alleymate.common.components.EmptyStateMessage
import com.mobdeve.s18.roman.isaacnathan.alleymate.ui.events.components.LiveEventCard

@Composable
fun HomeScreen(
    onNavigateToEvents: () -> Unit,
    onNavigateToLiveSale: (eventId: Int) -> Unit,
    viewModel: HomeViewModel = viewModel()
) {

    val upcomingEvents by viewModel.upcomingEvents.collectAsState()
    val liveEvent by viewModel.liveEvent.collectAsState()

    Scaffold(
        topBar = {
            HomeTopBar(title = "Alleymate")
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // ─── UPCOMING EVENTS SECTION ───────────────────────
            item {
                UpcomingEventsSection(
                    events = upcomingEvents, // Pass the dynamic list
                    onViewAllClick = onNavigateToEvents
                )
            }

            // ─── CURRENT LIVE SALE SECTION ─────────────────────
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    SectionHeader(
                        title = "Current Live Sale",
                        showDivider = true,
                        isSubtle = true
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    val currentLiveEvent = liveEvent
                    if (currentLiveEvent != null) {
                        LiveEventCard(
                            event = currentLiveEvent,
                            onEventClick = { eventId ->
                                onNavigateToLiveSale(eventId)
                            }
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

            // ─── OVERVIEW SECTION ──────────────────────────────
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    SectionHeader(
                        title = "Overview",
                        showDivider = true,
                        isSubtle = true
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OverviewGrid()
                }
            }
        }
    }
}
