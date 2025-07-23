package com.mobdeve.s18.roman.isaacnathan.alleymate.ui.reports

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mobdeve.s18.roman.isaacnathan.alleymate.common.components.AppTopBar
import com.mobdeve.s18.roman.isaacnathan.alleymate.common.components.EmptyStateMessage
import com.mobdeve.s18.roman.isaacnathan.alleymate.common.components.SectionHeader
import com.mobdeve.s18.roman.isaacnathan.alleymate.ui.reports.components.BestSellerListItem
import com.mobdeve.s18.roman.isaacnathan.alleymate.ui.reports.components.ReportFilters
import com.mobdeve.s18.roman.isaacnathan.alleymate.ui.reports.components.StatsGrid
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.Event

@Composable
fun ReportsScreen(
    viewModel: ReportsViewModel = viewModel()
) {
    // Observe reactive state from the ViewModel
    val allEvents by viewModel.allEvents.collectAsState()
    val filterState by viewModel.filterState.collectAsState()
    val stats by viewModel.reportStats.collectAsState()
    val bestSellers by viewModel.bestSellers.collectAsState()

    // Create a custom "All Events" option for the dropdown menu
    val allEventsOption = remember { Event(eventId = -1, title = "All Events", location = "", startDate = 0, endDate = 0) }
    val eventsForSelector = listOf(allEventsOption) + allEvents

    Scaffold(
        topBar = {
            AppTopBar(title = "Reports")
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Filter dropdowns and time range selection
            item {
                ReportFilters(
                    events = eventsForSelector,
                    selectedEvent = filterState.selectedEvent ?: allEventsOption,
                    onEventSelected = { event ->
                        // Convert "All Events" selection to null
                        viewModel.selectEvent(if (event.eventId == -1) null else event)
                    },
                    selectedTimeFilter = filterState.selectedTimeFilter,
                    onTimeFilterSelected = { timeFilter ->
                        viewModel.selectTimeFilter(timeFilter)
                    }
                )
            }

            // Divider line between filters and stats
            item {
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    color = MaterialTheme.colorScheme.outlineVariant
                )
            }

            // Stats section with dynamic title based on filters
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    val eventName = filterState.selectedEvent?.title ?: "All Events"
                    val timeFrame = filterState.selectedTimeFilter.displayName
                    SectionHeader(
                        title = "Stats for $eventName ($timeFrame)",
                        isSubtle = true
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    StatsGrid(stats = stats)
                }
            }

            // Header for best sellers
            item {
                SectionHeader(
                    title = "Best Sellers",
                    modifier = Modifier.padding(horizontal = 16.dp),
                    showDivider = true,
                    isSubtle = true
                )
            }

            // Show empty state if no data; otherwise render list of best sellers
            if (bestSellers.isEmpty()) {
                item {
                    EmptyStateMessage(
                        title = "No Sales Data",
                        subtitle = "No sales recorded for the selected period.",
                        modifier = Modifier.padding(horizontal = 16.dp),
                        titleColor = Color.Black,
                        subtitleColor = Color.Gray
                    )
                }
            } else {
                items(
                    items = bestSellers,
                    key = { it.rank }
                ) { bestSeller ->
                    BestSellerListItem(
                        bestSeller = bestSeller,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }
        }
    }
}