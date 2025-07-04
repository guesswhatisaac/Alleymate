package com.mobdeve.s18.roman.isaacnathan.alleymate.ui.allocate

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mobdeve.s18.roman.isaacnathan.alleymate.common.components.AppTopBar
import com.mobdeve.s18.roman.isaacnathan.alleymate.common.components.SectionHeader
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.CatalogueItem
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.Event
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.EventStatus
import com.mobdeve.s18.roman.isaacnathan.alleymate.theme.AlleyMainOrange
import com.mobdeve.s18.roman.isaacnathan.alleymate.theme.AlleyMateTheme
import com.mobdeve.s18.roman.isaacnathan.alleymate.ui.allocate.components.AllocationItem
import com.mobdeve.s18.roman.isaacnathan.alleymate.common.components.EventSelectorBar

@Composable
fun AllocateScreen(onNavigateBack: () -> Unit) {

    val allEvents = listOf(
        Event(1, "KOMIKET ‘25", "Oct 25-27, 2025", "", EventStatus.UPCOMING, 0, 0, 0, 0),
        Event(2, "Sticker Con '25", "Nov 16, 2025", "", EventStatus.UPCOMING, 0, 0, 0, 0)
    )
    var selectedEvent by remember { mutableStateOf(allEvents[0]) }

    val itemsToAllocate = listOf(
        CatalogueItem(1, "MHYLOW star sticker", "Sticker", 100.0, 50),
        CatalogueItem(2, "Art Print A", "Print", 100.0, 30),
        CatalogueItem(3, "Cosmic Enamel Pin", "Jewelry", 100.0, 25)
    )

    Scaffold(
        topBar = {
            AppTopBar(
                title = "Allocate",
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(top = 16.dp, bottom = 96.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Event Selector
                item {
                    EventSelectorBar(
                        currentEventName = selectedEvent.title,
                        currentEventDate = selectedEvent.date,
                        events = allEvents,
                        onEventSelected = { selectedEvent = it },
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }

                // Header
                item {
                    SectionHeader(
                        title = "${itemsToAllocate.size} Designs Selected",
                        showDivider = true,
                        isSubtle = true,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }

                // List of items to allocate
                items(itemsToAllocate) { item ->
                    AllocationItem(
                        item = item,
                        onQuantityChange = { newQuantity ->
                            // TODO: Update a ViewModel with the new quantity for this item
                            println("Item ${item.itemId} quantity changed to $newQuantity")
                        },
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }

            // "Allocate" Button at the bottom
            Button(
                onClick = { /* TODO: Handle final allocation logic */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .align(
                        alignment = Alignment.BottomEnd
                    )
                    .padding(16.dp),
                shape = MaterialTheme.shapes.medium,
                colors = ButtonDefaults.buttonColors(containerColor = AlleyMainOrange)
            ) {
                Text(
                    text = "ALLOCATE",
                    modifier = Modifier.padding(vertical = 8.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AllocateScreenPreview() {
    AlleyMateTheme {
        AllocateScreen(onNavigateBack = {})
    }
}