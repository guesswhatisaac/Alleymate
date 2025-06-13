package com.mobdeve.s18.roman.isaacnathan.alleymate.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mobdeve.s18.roman.isaacnathan.alleymate.common.components.HomeTopBar
import com.mobdeve.s18.roman.isaacnathan.alleymate.common.components.SectionHeader
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.Event
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.EventStatus
import com.mobdeve.s18.roman.isaacnathan.alleymate.ui.home.components.CurrentLiveSaleCard
import com.mobdeve.s18.roman.isaacnathan.alleymate.ui.home.components.OverviewGrid
import com.mobdeve.s18.roman.isaacnathan.alleymate.ui.home.components.UpcomingEventsSection

@Composable
fun HomeScreen() {
    Scaffold(
        topBar = {
            HomeTopBar(title = "Alleymate")
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // ─── UPCOMING EVENTS SECTION ───────────────────────
            item {
                UpcomingEventsSection(events = getSampleEvents())
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
                    CurrentLiveSaleCard()
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

// ─────────────────────────────────────────────────────────────
// Preview
// ─────────────────────────────────────────────────────────────
@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}
