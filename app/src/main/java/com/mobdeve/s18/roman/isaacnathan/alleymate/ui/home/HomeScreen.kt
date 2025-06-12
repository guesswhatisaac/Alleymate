package com.mobdeve.s18.roman.isaacnathan.alleymate.ui.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mobdeve.s18.roman.isaacnathan.alleymate.common.components.HomeTopBar

@Composable
fun HomeScreen() {
    Scaffold(
        topBar = {
            HomeTopBar(title = "Alleymate")
        }
    ) { innerPadding ->
        // CHANGE 1: Remove contentPadding from LazyColumn.
        // It will now provide its children with the full screen width.
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // --- UPCOMING EVENTS SECTION ---
            item {
                // CHANGE 2: Call UpcomingEventsSection with NO special modifier.
                // It will now correctly fill the full width it is given.
                UpcomingEventsSection(events = getSampleEvents())
            }

            // --- CURRENT LIVE SALE SECTION ---
            item {
                // CHANGE 3: Apply padding *inside* the item that needs it.
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    Text(
                        text = "Current Live Sale",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    CurrentLiveSaleCard()
                }
            }

            // --- OVERVIEW SECTION ---
            item {
                // CHANGE 4: Apply padding *inside* this item as well.
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    Text(
                        text = "Overview",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OverviewGrid()
                }
            }
        }
    }
}

// Helper composable for the 2x2 grid of stats
@Composable
private fun OverviewGrid() {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            OverviewStatCard(value = "₱4500", label = "Last Event Sales", modifier = Modifier.weight(1f))
            OverviewStatCard(value = "₱7525.25", label = "Average Profit per Event", modifier = Modifier.weight(1f))
        }
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            OverviewStatCard(value = "10 Items", label = "Products in Catalogue", modifier = Modifier.weight(1f))
            OverviewStatCard(value = "5 Items", label = "Low in Stock", modifier = Modifier.weight(1f))
        }
    }
}

private fun getSampleEvents(): List<Event> {
    return listOf(
        Event(1, "KOMIKET ‘25", "October 25 - October 27", "SM Megamall B | Table 35"),
        Event(2, "STICKER CON", "November 16", "Whitespace Manila"),
        Event(3, "COSMANIA", "December 7 - December 8", "SMX Convention Center")
    )
}

// NO CHANGES NEEDED HERE ANYMORE.
// It now correctly takes an optional modifier and applies it.
@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun UpcomingEventsSection(
    modifier: Modifier = Modifier,
    events: List<Event>
) {
    val pagerState = rememberPagerState(pageCount = { events.size })
    val purpleBackgroundColor = Color(0xFF9A31BB)
    val indicatorActiveColor = Color(0xFFff8a65)
    val indicatorInactiveColor = Color(0xFFc4c4c4)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(purpleBackgroundColor)
            .padding(vertical = 24.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "UPCOMING EVENTS",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "View all events",
                tint = Color.White,
                modifier = Modifier.size(28.dp)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        HorizontalPager(
            state = pagerState,
            contentPadding = PaddingValues(horizontal = 40.dp),
            pageSpacing = 16.dp
        ) { page ->
            EventCard(event = events[page])
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(pagerState.pageCount) { iteration ->
                val color = if (pagerState.currentPage == iteration) indicatorActiveColor else indicatorInactiveColor
                Box(
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .clip(CircleShape)
                        .background(color)
                        .size(10.dp)
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}