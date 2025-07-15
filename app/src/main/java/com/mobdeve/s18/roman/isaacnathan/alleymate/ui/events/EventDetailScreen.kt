package com.mobdeve.s18.roman.isaacnathan.alleymate.ui.events

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
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
import androidx.navigation.NavController
import com.mobdeve.s18.roman.isaacnathan.alleymate.common.components.AppTopBar
import com.mobdeve.s18.roman.isaacnathan.alleymate.common.components.AppFloatingActionButton
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.local.AlleyMateDatabase
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.Event
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.EventExpense
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.EventStatus
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.relations.EventInventoryWithDetails
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.repository.EventRepository
import com.mobdeve.s18.roman.isaacnathan.alleymate.theme.AlleyMainOrange
import com.mobdeve.s18.roman.isaacnathan.alleymate.ui.AppDestinations
import com.mobdeve.s18.roman.isaacnathan.alleymate.ui.events.components.EditEventModal
import com.mobdeve.s18.roman.isaacnathan.alleymate.ui.events.components.AddExpenseModal
import com.mobdeve.s18.roman.isaacnathan.alleymate.ui.events.components.DeleteEventModal

private enum class DetailTab(val title: String) {
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
    navController: NavController,
    onNavigateToLiveSale: (Int) -> Unit
) {
    // --- Get Dependencies and ViewModel ---
    val context = LocalContext.current
    val eventRepository = remember {
        EventRepository(
            AlleyMateDatabase.getDatabase(context).eventDao(),
            catalogueDao = AlleyMateDatabase.getDatabase(context).catalogueDao(),
            transactionDao = AlleyMateDatabase.getDatabase(context).transactionDao()
        )
    }
    val viewModel: EventDetailViewModel = viewModel(
        factory = EventDetailViewModelFactory(eventRepository, eventId)
    )

    val event by viewModel.event.collectAsState()
    val inventory by viewModel.inventory.collectAsState()
    val expenses by viewModel.expenses.collectAsState()

    // Tab state
    var selectedTab by remember { mutableStateOf(DetailTab.INVENTORY) }

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

    val startSaleError by viewModel.startSaleError.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(startSaleError) {
        startSaleError?.let { error ->
            snackbarHostState.showSnackbar(
                message = error,
                duration = SnackbarDuration.Short
            )
            viewModel.onStartSaleErrorShown()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            AppTopBar(
                title = event?.title ?: "Event Details",
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, tint = Color.White, contentDescription = "Navigate back")
                    }
                },
                actions = {
                    // Start Live Sale button (only for upcoming events)
                    if (event?.status == EventStatus.UPCOMING) {
                        IconButton(
                            onClick = {
                                viewModel.startLiveSale(onSuccess = {
                                    navController.navigate("${AppDestinations.LIVE_SALE_ROUTE}/${event!!.eventId}") {
                                        popUpTo("${AppDestinations.EVENT_DETAIL_ROUTE}/${event!!.eventId}") {
                                            inclusive = true
                                        }
                                    }
                                })
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = "Start Live Sale",
                                tint = Color.White
                            )
                        }
                    }

                    // More options menu
                    Box {
                        IconButton(onClick = { menuExpanded = true }) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "More Options",
                                tint = Color.White
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
                // Event Stats Header
                EventStatsHeader(
                    event = currentEvent,
                    expenses = expenses
                )

                // Tabs Section
                TabRow(
                    selectedTabIndex = selectedTab.ordinal,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    DetailTab.entries.forEach { tab ->
                        Tab(
                            selected = selectedTab == tab,
                            onClick = { selectedTab = tab },
                            text = {
                                Text(tab.title)
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

@Composable
private fun EventStatsHeader(
    event: Event,
    expenses: List<EventExpense>
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        border = BorderStroke(1.dp, Color.Gray.copy(alpha = 0.3f)),
        shape = RoundedCornerShape(12.dp)
    ) {
        // Stats grid
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            StatCard(
                modifier = Modifier.weight(1f),
                value = "${event.totalItemsSold}",
                label = "Sold"
            )
            StatCard(
                modifier = Modifier.weight(1f),
                value = "₱${"%.0f".format(event.totalRevenueInCents / 100.0)}",
                label = "Revenue"
            )
            StatCard(
                modifier = Modifier.weight(1f),
                value = "₱${"%.0f".format(expenses.sumOf { it.amountInCents } / 100.0)}",
                label = "Expenses"
            )
            StatCard(
                modifier = Modifier.weight(1f),
                value = "₱${"%.0f".format(event.profitInCents / 100.0)}",
                label = "Profit"
            )
        }
    }
}

@Composable
private fun StatCard(
    modifier: Modifier = Modifier,
    value: String,
    label: String
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = AlleyMainOrange
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = Color.Gray
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
