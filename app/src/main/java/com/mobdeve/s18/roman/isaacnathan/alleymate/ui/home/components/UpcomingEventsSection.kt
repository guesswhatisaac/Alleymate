package com.mobdeve.s18.roman.isaacnathan.alleymate.ui.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.Event
import androidx.compose.foundation.clickable
import androidx.compose.material3.Card
import androidx.compose.ui.text.font.FontWeight
import com.mobdeve.s18.roman.isaacnathan.alleymate.common.components.EmptyStateMessage

@Composable
fun UpcomingEventsSection(
    modifier: Modifier = Modifier,
    events: List<Event>,
    onViewAllClick: () -> Unit
) {


    // ─── Colors ───────────────────────────────────────────────
    val purpleBackgroundColor = Color(0xFF9A31BB)
    val indicatorActiveColor = Color(0xFFff8a65)
    val indicatorInactiveColor = Color(0xFFc4c4c4)

    // ─── Section Container ────────────────────────────────────
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(purpleBackgroundColor)
            .padding(vertical = 24.dp)
    ) {
        // ─── Section Header ───────────────────────────────────
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .clickable { onViewAllClick() },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "UPCOMING EVENTS",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White
            )
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "View all events",
                tint = Color.White,
                modifier = Modifier.size(28.dp),

            )
        }

        Spacer(modifier = Modifier.height(20.dp))
        if (events.isEmpty()) {
            EmptyStateMessage(
                title = "No Upcoming Events",
                subtitle = "Tap '+' button in the Events tab to create one.",
                titleColor = Color.White,
                subtitleColor = Color.White.copy(alpha = 0.8f),
                modifier = Modifier.padding(horizontal = 24.dp)
            )
        } else {
            val pagerState = rememberPagerState(pageCount = { events.size })

            // Events Pager
            HorizontalPager(
                state = pagerState,
                contentPadding = PaddingValues(horizontal = 40.dp),
                pageSpacing = 16.dp
            ) { page ->
                EventCard(event = events[page])
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Page Indicators
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(pagerState.pageCount) { index ->
                    val color = if (pagerState.currentPage == index) indicatorActiveColor else indicatorInactiveColor
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .clip(MaterialTheme.shapes.extraLarge)
                            .background(color)
                            .size(10.dp)
                    )
                }
            }
        }
    }
}


@Composable
private fun EventCard(event: Event, modifier: Modifier = Modifier) {
    Card(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = event.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = event.dateRangeString,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
    }
}