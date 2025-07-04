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
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.Event
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.EventStatus
import com.mobdeve.s18.roman.isaacnathan.alleymate.ui.home.components.CurrentLiveSaleCard
import com.mobdeve.s18.roman.isaacnathan.alleymate.ui.home.components.OverviewGrid
import com.mobdeve.s18.roman.isaacnathan.alleymate.ui.home.components.UpcomingEventsSection
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun HomeScreen(
    onNavigateToEvents: () -> Unit,
    onNavigateToLiveSale: () -> Unit,
    viewModel: HomeViewModel = viewModel()
) {
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
                    events = getSampleEvents(),
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
                    CurrentLiveSaleCard(onContinueClick = onNavigateToLiveSale)
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

// ─────────────────────────────────────────────────────────────
// Sample event data for preview/testing
// ─────────────────────────────────────────────────────────────
private fun getSampleEvents(): List<Event> {
    return listOf(
        Event(1, "KOMIKET ‘25", "October 25 - October 27", "SM Megamall B | Table 35", EventStatus.LIVE, 0, 0, 0, 0),
        Event(2, "STICKER CON", "November 16", "Whitespace Manila", EventStatus.LIVE, 0, 0, 0, 0),
        Event(3, "COSMANIA", "December 7 - December 8", "SMX Convention Center", EventStatus.LIVE, 0, 0, 0, 0)
    )
}

