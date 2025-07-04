package com.mobdeve.s18.roman.isaacnathan.alleymate.ui.reports

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mobdeve.s18.roman.isaacnathan.alleymate.common.components.AppTopBar
import com.mobdeve.s18.roman.isaacnathan.alleymate.common.components.SectionHeader
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.CatalogueItem
import com.mobdeve.s18.roman.isaacnathan.alleymate.theme.AlleyMateTheme
import com.mobdeve.s18.roman.isaacnathan.alleymate.ui.catalogue.components.CatalogueItemCard
import com.mobdeve.s18.roman.isaacnathan.alleymate.common.components.EventSelectorBar
import com.mobdeve.s18.roman.isaacnathan.alleymate.ui.reports.components.TimeFilterChips
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.Event
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.EventStatus

@Composable
fun ReportsScreen() {

    val allEvents = listOf(
        Event(1, "KOMIKET ‘25", "Oct 25-27", "loc", EventStatus.LIVE, 0,0,0,0),
        Event(2, "Sticker Con", "Nov 16", "loc", EventStatus.UPCOMING, 0,0,0,0)
    )
    var selectedEvent by remember { mutableStateOf(allEvents[0]) }

    Scaffold(
        topBar = {
            AppTopBar(title = "Reports")
        }
    ) { innerPadding ->
        // Dummy data
        val bestSellers = listOf(
            CatalogueItem(1, "MHYLOW star sticker", "Sticker", 100.0, 50),
            CatalogueItem(2, "MHYLOW star sticker", "Sticker", 100.0, 50),
            CatalogueItem(3, "MHYLOW star sticker", "Sticker", 100.0, 50)
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 80.dp)
        ) {
            // --- EVENT SELECTOR BAR ---
            item {
                EventSelectorBar(
                    currentEventName = selectedEvent.title,
                    currentEventDate = selectedEvent.date,
                    events = allEvents,
                    onEventSelected = { newEvent ->
                        selectedEvent = newEvent
                    }
                )
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    color = MaterialTheme.colorScheme.outlineVariant
                )
            }

            // --- TIME FILTER CHIPS ---
            item {
                TimeFilterChips()
            }

            // --- REPORT STATS ---
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    SectionHeader(
                        title = "Report for ALL TIME",
                        showDivider = true,
                        isSubtle = true
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        StatColumn(value = "₱4500", label = "ALL TIME Revenue")
                        StatColumn(value = "₱4500", label = "ALL TIME Expenses")
                        StatColumn(value = "₱4500", label = "ALL TIME Profit")
                    }
                }
            }

            // --- STOCK HISTORY ---
            /*
            item {
                Column {
                    SectionHeader(
                        title = "ALL-TIME Stock History",
                        modifier = Modifier.padding(horizontal = 16.dp),
                        showDivider = true,
                        isSubtle = true
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp)
                    ) { }
                }
            }
            */

            // --- BEST SELLERS ---
            item {
                Column {
                    SectionHeader(
                        title = "ALL-TIME Best Sellers",
                        modifier = Modifier.padding(horizontal = 16.dp),
                        showDivider = true,
                        isSubtle = true
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp)
                    ) {
                        items(bestSellers) { item ->
                            Box(modifier = Modifier.width(180.dp)) {
                                CatalogueItemCard(
                                    item = item,
                                    // TODO: BEST SELLER CATALOGUE ITEM CARD NO LOGIC YET
                                    onRestockClick = {},
                                    onEditClick = {},
                                    onDeleteClick = {}
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 400)
@Composable
private fun ReportsScreenPreview() {
    AlleyMateTheme {
        ReportsScreen()
    }
}

@Composable
private fun StatColumn(value: String, label: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Black,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = Color.Gray
        )
    }
}
