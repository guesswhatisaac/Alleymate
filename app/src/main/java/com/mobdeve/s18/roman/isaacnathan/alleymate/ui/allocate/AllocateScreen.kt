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
import com.mobdeve.s18.roman.isaacnathan.alleymate.theme.AlleyMainOrange
import com.mobdeve.s18.roman.isaacnathan.alleymate.theme.AlleyMateTheme
import com.mobdeve.s18.roman.isaacnathan.alleymate.ui.allocate.components.AllocationItem
import com.mobdeve.s18.roman.isaacnathan.alleymate.common.components.EventSelectorBar
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun AllocateScreen(
    onNavigateBack: () -> Unit,
    viewModel: AllocationViewModel = viewModel()
) {

    val allEvents by viewModel.allEvents.collectAsState()
    val selectedEvent by viewModel.selectedEvent.collectAsState()
    val itemsToAllocate by viewModel.itemsToAllocate.collectAsState()
    val allocationQuantities by viewModel.allocationQuantities.collectAsState()

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
                    selectedEvent?.let { event ->
                        EventSelectorBar(
                            currentEventName = event.title,
                            currentEventDate = event.dateRangeString,
                            events = allEvents,
                            onEventSelected = viewModel::selectEvent,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }
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
                items(itemsToAllocate, key = { it.itemId }) { item ->
                    AllocationItem(
                        item = item,
                        // Update the quantity in the ViewModel
                        onQuantityChange = { newQuantity ->
                            viewModel.updateAllocationQuantity(item.itemId, newQuantity)
                        },
                        onRemoveClick = {
                            viewModel.removeAllocationItem(item.itemId)
                        },
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }

            // "Allocate" Button at the bottom
            Button(
                onClick = {
                    viewModel.performAllocation(onSuccess = onNavigateBack)
                },
                enabled = selectedEvent != null,
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