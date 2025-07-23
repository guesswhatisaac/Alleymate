package com.mobdeve.s18.roman.isaacnathan.alleymate.ui.allocate

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mobdeve.s18.roman.isaacnathan.alleymate.common.components.*
import com.mobdeve.s18.roman.isaacnathan.alleymate.theme.AlleyMainOrange
import com.mobdeve.s18.roman.isaacnathan.alleymate.ui.allocate.components.AllocationItem

@Composable
fun AllocateScreen(
    onNavigateBack: () -> Unit,
    viewModel: AllocationViewModel = viewModel()
) {
    // --- State Observers ---
    val allEvents by viewModel.allEvents.collectAsState()
    val selectedEvent by viewModel.selectedEvent.collectAsState()
    val itemsToAllocate by viewModel.itemsToAllocate.collectAsState()
    val isAllocationValid by viewModel.isAllocationValid.collectAsState()

    Scaffold(
        topBar = {
            AppTopBar(
                title = "Allocate",
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            // --- Item List Section ---
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(top = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Event selector bar
                item {
                    EventSelectorBar(
                        currentEvent = selectedEvent,
                        events = allEvents,
                        onEventSelected = viewModel::selectEvent,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }

                // --- Empty State or Item List ---
                if (itemsToAllocate.isEmpty()) {
                    item {
                        SectionHeader(
                            title = "No Items Selected",
                            showDivider = true,
                            isSubtle = true,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )

                        Spacer(Modifier.height(16.dp))

                        EmptyStateMessage(
                            title = "Nothing to Allocate",
                            subtitle = "Long-press a catalogue item to allocate your first product.",
                            titleColor = Color.Black,
                            subtitleColor = Color.Gray
                        )
                    }
                } else {
                    // Items header
                    item {
                        SectionHeader(
                            title = "${itemsToAllocate.size} Items Selected",
                            showDivider = true,
                            isSubtle = true,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }

                    // Item cards
                    items(itemsToAllocate, key = { it.itemId }) { item ->
                        AllocationItem(
                            item = item,
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
            }

            // --- Validation Message ---
            if (itemsToAllocate.isNotEmpty() && !isAllocationValid) {
                Text(
                    text = "All items must have a quantity greater than '0'.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 4.dp)
                )
            }

            // --- Allocate Button ---
            Button(
                onClick = {
                    viewModel.performAllocation(onSuccess = onNavigateBack)
                },
                enabled = selectedEvent != null && isAllocationValid,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
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
