package com.mobdeve.s18.roman.isaacnathan.alleymate.ui.live_sale

import android.app.Application
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mobdeve.s18.roman.isaacnathan.alleymate.common.components.*
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.Event
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.EventExpense
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.EventStatus
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.relations.EventInventoryWithDetails
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.relations.TransactionWithItems
import com.mobdeve.s18.roman.isaacnathan.alleymate.ui.live_sale.components.TransactionListItem
import com.mobdeve.s18.roman.isaacnathan.alleymate.theme.AlleyMainOrange

private enum class LiveSaleTab(val title: String) {
    TRANSACTIONS("Transactions"),
    INVENTORY("Inventory"),
    EXPENSES("Expenses")
}

@Composable
fun LiveSaleScreen(
    eventId: Int,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val viewModel: LiveSaleViewModel = viewModel(
        factory = LiveSaleViewModelFactory(context.applicationContext as Application, eventId)
    )

    val event by viewModel.event.collectAsState()
    val inventory by viewModel.inventory.collectAsState()
    val expenses by viewModel.expenses.collectAsState()
    val transactions by viewModel.transactions.collectAsState()

    var selectedTab by remember { mutableStateOf(LiveSaleTab.TRANSACTIONS) }
    var showEndEventDialog by remember { mutableStateOf(false) }

    // End Event Confirmation Dialog
    if (showEndEventDialog) {
        AlertDialog(
            onDismissRequest = { showEndEventDialog = false },
            title = { Text("End Live Sale") },
            text = { Text("Are you sure you want to end this live sale? This action cannot be undone.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        // TODO: Call viewModel.endLiveSale()
                        showEndEventDialog = false
                        onNavigateBack()
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = Color(0xFFF44336)
                    )
                ) {
                    Text("End Sale")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEndEventDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            LiveSaleTopBar(
                title = event?.title ?: "Live Sale",
                onNavigateBack = onNavigateBack,
                actions = {
                    // End Event Button in top bar
                    if (event?.status == EventStatus.LIVE) {
                        IconButton(onClick = { showEndEventDialog = true }) {
                            Icon(
                                imageVector = Icons.Default.Stop,
                                contentDescription = "End Sale",
                                tint = Color.White
                            )
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            if (selectedTab == LiveSaleTab.TRANSACTIONS) {
                AppFloatingActionButton(
                    onClick = { /* TODO: Open Add Transaction Modal */ }
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
                // Live Sale Stats Header (always visible)
                LiveSaleStatsHeader(
                    event = currentEvent,
                    expenses = expenses
                )

                // Three-tab layout
                TabRow(
                    selectedTabIndex = selectedTab.ordinal,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    LiveSaleTab.entries.forEach { tab ->
                        Tab(
                            selected = selectedTab == tab,
                            onClick = { selectedTab = tab },
                            text = {
                                Text(tab.title)
                            }
                        )
                    }
                }

                // Tab Content with consistent padding
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    when (selectedTab) {
                        LiveSaleTab.TRANSACTIONS -> {
                            TransactionsContent(
                                event = currentEvent,
                                transactions = transactions
                            )
                        }
                        LiveSaleTab.INVENTORY -> {
                            EventInventorySection(inventory = inventory)
                        }
                        LiveSaleTab.EXPENSES -> {
                            EventExpensesSection(expenses = expenses)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LiveSaleStatsHeader(
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
private fun TransactionsContent(
    event: Event,
    transactions: List<TransactionWithItems>
) {
    if (transactions.isEmpty()) {
        EmptyStateMessage(
            title = "No Transactions Yet",
            subtitle = "Tap the '+' button to record your first sale.",
            modifier = Modifier.fillMaxSize(),
            titleColor = Color.Black,
            subtitleColor = Color.Gray
        )
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 80.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(
                items = transactions,
                key = { it.transaction.transactionId }
            ) { transactionWithItems ->
                TransactionListItem(transactionWithItems = transactionWithItems)
            }
        }
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
