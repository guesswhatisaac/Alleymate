package com.mobdeve.s18.roman.isaacnathan.alleymate.ui.events

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mobdeve.s18.roman.isaacnathan.alleymate.common.components.AppTopBar
import com.mobdeve.s18.roman.isaacnathan.alleymate.common.components.AppFloatingActionButton
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.local.AlleyMateDatabase
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.Event
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.EventExpense
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.EventStatus
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.relations.EventInventoryWithDetails
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.repository.EventRepository
import com.mobdeve.s18.roman.isaacnathan.alleymate.theme.AlleyMainOrange
import com.mobdeve.s18.roman.isaacnathan.alleymate.ui.events.components.EditEventModal
import com.mobdeve.s18.roman.isaacnathan.alleymate.ui.events.components.AddExpenseModal
import com.mobdeve.s18.roman.isaacnathan.alleymate.ui.events.components.DeleteEventModal

private enum class DetailTab(val title: String) {
    OVERVIEW("Overview"),
    INVENTORY("Inventory"),
    EXPENSES("Expenses")
}

private sealed interface EventDetailModalState {
    data object None : EventDetailModalState
    data class EditEvent(val event: Event) : EventDetailModalState
    data object DeleteConfirmation : EventDetailModalState
    data object AddExpense : EventDetailModalState
}

@Composable
fun EventDetailScreen(
    eventId: Int,
    onNavigateBack: () -> Unit,
    onNavigateToLiveSale: (Int) -> Unit
) {
    // --- Get Dependencies and ViewModel ---
    val context = LocalContext.current
    val eventRepository = remember {
        EventRepository(
            AlleyMateDatabase.getDatabase(context).eventDao(),
            catalogueDao = AlleyMateDatabase.getDatabase(context).catalogueDao()
        )
    }
    val viewModel: EventDetailViewModel = viewModel(
        factory = EventDetailViewModelFactory(eventRepository, eventId)
    )

    val event by viewModel.event.collectAsState()
    val inventory by viewModel.inventory.collectAsState()
    val expenses by viewModel.expenses.collectAsState()

    // Tab state
    var selectedTab by remember { mutableStateOf(DetailTab.OVERVIEW) }

    // Modal state
    var modalState by remember { mutableStateOf<EventDetailModalState>(EventDetailModalState.None) }
    var menuExpanded by remember { mutableStateOf(false) }

    // Handle modals
    when (val state = modalState) {
        is EventDetailModalState.None -> { /* Show nothing */ }
        is EventDetailModalState.EditEvent -> {
            EditEventModal(
                event = state.event,
                onDismissRequest = { modalState = EventDetailModalState.None },
                onConfirmEdit = { updatedEvent ->
                    viewModel.updateEvent(updatedEvent)
                    modalState = EventDetailModalState.None
                }
            )
        }
        is EventDetailModalState.DeleteConfirmation -> {
            event?.let {
                DeleteEventModal(
                    event = it,
                    onDismissRequest = { modalState = EventDetailModalState.None },
                    onConfirmDelete = {
                        viewModel.deleteEvent()
                        modalState = EventDetailModalState.None
                        onNavigateBack()
                    }
                )
            }
        }
        is EventDetailModalState.AddExpense -> {
            AddExpenseModal(
                onDismissRequest = { modalState = EventDetailModalState.None },
                onAddExpense = { description, amount ->
                    viewModel.addExpense(description, amount)
                    modalState = EventDetailModalState.None
                }
            )
        }
    }

    Scaffold(
        topBar = {
            AppTopBar(
                title = event?.title ?: "Event Details",
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, tint = Color.White, contentDescription = "Navigate back")
                    }
                },
                actions = {
                    // More options menu
                    Box {
                        IconButton(onClick = { menuExpanded = true }) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "More Options"
                            )
                        }
                        DropdownMenu(
                            expanded = menuExpanded,
                            onDismissRequest = { menuExpanded = false },
                            modifier = Modifier
                                .width(120.dp)
                                .background(MaterialTheme.colorScheme.surface),
                            properties = PopupProperties(focusable = true)
                        ) {
                            DropdownMenuItem(
                                text = { Text("Edit") },
                                enabled = event?.status != EventStatus.ENDED,
                                onClick = {
                                    event?.let { modalState = EventDetailModalState.EditEvent(it) }
                                    menuExpanded = false
                                },
                                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                            )
                            DropdownMenuItem(
                                text = {
                                    Text("Delete", color = MaterialTheme.colorScheme.error)
                                },
                                onClick = {
                                    modalState = EventDetailModalState.DeleteConfirmation
                                    menuExpanded = false
                                },
                                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                            )
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            if (selectedTab == DetailTab.EXPENSES) {
                AppFloatingActionButton(
                    onClick = { modalState = EventDetailModalState.AddExpense }
                )
            }
        }
    ) { innerPadding ->
        val currentEvent = event
        if (currentEvent == null) {
            Box(
                Modifier.fillMaxSize().padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                // Tabs Section
                Column {
                    TabRow(
                        selectedTabIndex = selectedTab.ordinal,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        DetailTab.entries.forEach { tab ->
                            val count = when (tab) {
                                DetailTab.OVERVIEW -> ""
                                DetailTab.INVENTORY -> " (${inventory.size})"
                                DetailTab.EXPENSES -> " (${expenses.size})"
                            }
                            Tab(
                                selected = selectedTab == tab,
                                onClick = { selectedTab = tab },
                                text = {
                                    Text("${tab.title}$count")
                                }
                            )
                        }
                    }

                    // Tab Content
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        when (selectedTab) {
                            DetailTab.OVERVIEW -> {
                                EventOverviewSection(
                                    event = currentEvent,
                                    onStartLiveSale = {
                                        viewModel.startLiveSale(onSuccess = {
                                            onNavigateToLiveSale(currentEvent.eventId)
                                        })
                                    },
                                    onEndEvent = {
                                        // TODO: Handle end event
                                        viewModel.endLiveSale()
                                    }
                                )
                            }
                            DetailTab.INVENTORY -> {
                                EventInventorySection(inventory = inventory)
                            }
                            DetailTab.EXPENSES -> {
                                EventExpensesSection(expenses = expenses)
                            }
                        }
                    }
                }
            }
        }
    }
}

// ====================================================================================
//  PRIVATE HELPER COMPOSABLES
// ====================================================================================

@Composable
private fun EventOverviewSection(
    event: Event,
    onStartLiveSale: () -> Unit,
    onEndEvent: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Event Details Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                DetailRow(label = "Date", value = event.dateRangeString)
                DetailRow(label = "Location", value = event.location)
                DetailRow(label = "Status", value = event.status.name)
            }
        }

        // Statistics Grid
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // First Row
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CompactStatCard(
                    modifier = Modifier.weight(1f),
                    value = "${event.totalItemsSold}",
                    label = "Items Sold"
                )
                CompactStatCard(
                    modifier = Modifier.weight(1f),
                    value = "₱${"%.2f".format(event.totalRevenueInCents / 100.0)}",
                    label = "Revenue"
                )
            }

            // Second Row
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CompactStatCard(
                    modifier = Modifier.weight(1f),
                    value = "₱${"%.2f".format(event.totalExpensesInCents / 100.0)}",
                    label = "Expenses"
                )
                CompactStatCard(
                    modifier = Modifier.weight(1f),
                    value = "₱${"%.2f".format(event.profitInCents / 100.0)}",
                    label = "Profit"
                )
            }
        }

        // Event Action Button
        when (event.status) {
            EventStatus.UPCOMING -> {
                Button(
                    onClick = onStartLiveSale,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AlleyMainOrange
                    )
                ) {
                    Text(
                        text = "Start Live Sale",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            EventStatus.LIVE -> {
                Button(
                    onClick = onEndEvent,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFF44336)
                    )
                ) {
                    Text(
                        text = "End Event",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            EventStatus.ENDED -> {
            }
        }
    }
}

@Composable
private fun CompactStatCard(
    modifier: Modifier = Modifier,
    value: String,
    label: String
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray
            )
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun EventInventorySection(inventory: List<EventInventoryWithDetails>) {
    if (inventory.isEmpty()) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
            shape = RoundedCornerShape(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No items allocated to this event",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }
        }
    } else {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column {
                inventory.forEachIndexed { index, inventoryItemWithDetails ->
                    val catalogueItem = inventoryItemWithDetails.catalogueItem
                    val eventInventory = inventoryItemWithDetails.eventInventoryItem

                    InventoryItem(
                        name = catalogueItem.name,
                        price = catalogueItem.price,
                        allocated = eventInventory.allocatedQuantity,
                        sold = eventInventory.soldQuantity
                    )

                    if (index < inventory.size - 1) {
                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            color = MaterialTheme.colorScheme.outlineVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun InventoryItem(
    name: String,
    price: Double,
    allocated: Int,
    sold: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = name,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "Allocated: $allocated | Sold: $sold",
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray
            )
        }
        Text(
            text = "₱${"%.2f".format(price)}",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = AlleyMainOrange
        )
    }
}

@Composable
private fun EventExpensesSection(expenses: List<EventExpense>) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Expenses List
        if (expenses.isEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                shape = RoundedCornerShape(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No expenses recorded",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                }
            }
        } else {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column {
                    expenses.forEachIndexed { index, expense ->
                        ExpenseItem(
                            description = expense.description,
                            amount = expense.amountInCents / 100.0
                        )

                        if (index < expenses.size - 1) {
                            HorizontalDivider(
                                modifier = Modifier.padding(horizontal = 16.dp),
                                color = MaterialTheme.colorScheme.outlineVariant
                            )
                        }
                    }
                }
            }
        }

        // Total Expenses Card
        if (expenses.isNotEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = AlleyMainOrange.copy(alpha = 0.1f)
                ),
                border = BorderStroke(1.dp, AlleyMainOrange.copy(alpha = 0.3f)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Total Expenses",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = AlleyMainOrange
                    )
                    Text(
                        text = "₱${"%.2f".format(expenses.sumOf { it.amountInCents } / 100.0)}",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = AlleyMainOrange
                    )
                }
            }
        }
    }
}

@Composable
private fun ExpenseItem(
    description: String,
    amount: Double
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = "₱${"%.2f".format(amount)}",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFF44336)
        )
    }
}
