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
import com.mobdeve.s18.roman.isaacnathan.alleymate.ui.home.components.CurrentLiveSaleCard
import com.mobdeve.s18.roman.isaacnathan.alleymate.ui.home.components.OverviewGrid
import com.mobdeve.s18.roman.isaacnathan.alleymate.ui.home.components.UpcomingEventsSection
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.*

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
                    events = upcomingEvents,
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
                    if (liveEvent != null) {
                        CurrentLiveSaleCard(
                            onContinueClick = {
                                onNavigateToLiveSale(liveEvent!!.eventId)
                            }
                        )
                    } else {
                        Text("No live sale currently active.", modifier = Modifier.padding(16.dp))
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

            // --- DEBUG SECTION ---
            item {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(top = 16.dp) // Add some space above the button
                ) {
                    Button(
                        onClick = {
                            // Call the ViewModel function
                            viewModel.deleteAllCatalogueItems()
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error,
                            contentColor = MaterialTheme.colorScheme.onError
                        )
                    ) {
                        Text(text = "DEBUG: DELETE ALL CATALOGUE ITEMS")
                    }
                    Button(
                        onClick = {
                            viewModel.restartDatabase()
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Black,
                            contentColor = MaterialTheme.colorScheme.error
                        ),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.error)
                    ) {
                        Text(text = "!! DEBUG: RESTART ENTIRE DATABASE !!")
                    }
                }
            }
        }
    }
}
