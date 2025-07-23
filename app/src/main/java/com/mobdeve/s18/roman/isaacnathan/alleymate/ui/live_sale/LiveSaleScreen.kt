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
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.EventExpense
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.EventStatus
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.relations.EventInventoryWithDetails
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.relations.TransactionWithItems
import com.mobdeve.s18.roman.isaacnathan.alleymate.theme.AlleyMainOrange
import com.mobdeve.s18.roman.isaacnathan.alleymate.ui.events.components.AddExpenseModal
import com.mobdeve.s18.roman.isaacnathan.alleymate.ui.live_sale.components.AddTransactionModal
import com.mobdeve.s18.roman.isaacnathan.alleymate.ui.live_sale.components.EndLiveSaleModal
import com.mobdeve.s18.roman.isaacnathan.alleymate.ui.live_sale.components.TransactionListItem

// Represents tabs in the live sale screen
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

    // Collect UI state and modal-related states
    val uiState by viewModel.uiState.collectAsState()
    val modalState by viewModel.modalState.collectAsState()
    val addTransactionState by viewModel.addTransactionState.collectAsState()
    val selectedItemsForTransaction by viewModel.selectedItemsForTransaction.collectAsState()
    val transactionCart by viewModel.transactionCart.collectAsState()

    var selectedTab by remember { mutableStateOf(LiveSaleTab.TRANSACTIONS) }
    var showEndEventDialog by remember { mutableStateOf(false) }

    // Display confirmation dialog for ending the event
    if (showEndEventDialog) {
        uiState.event?.let {
            EndLiveSaleModal(
                event = it,
                onDismissRequest = { showEndEventDialog = false },
                onConfirmEndSale = {
                    viewModel.endLiveSale()
                    showEndEventDialog = false
                    onNavigateBack()
                }
            )
        }
    }

    // Show modal dialogs based on current modal state
    when (modalState) {
        LiveSaleModalState.Hidden -> {}
        LiveSaleModalState.AddTransaction -> {
            if (addTransactionState != AddTransactionState.Hidden) {
                AddTransactionModal(
                    inventory = uiState.inventory,
                    selectedItemIds = selectedItemsForTransaction,
                    transactionCart = transactionCart,
                    isTransacting = addTransactionState == AddTransactionState.Transacting,
                    onDismiss = viewModel::dismissAddTransaction,
                    onItemSelect = viewModel::toggleItemSelection,
                    onProceed = viewModel::proceedToTransact,
                    onQuantityChange = viewModel::updateTransactionQuantity,
                    onConfirmTransaction = viewModel::recordSale
                )
            }
        }
        LiveSaleModalState.AddExpense -> {
            AddExpenseModal(
                onDismissRequest = viewModel::dismissAllModals,
                onAddExpense = { description, amount ->
                    viewModel.addExpense(description, amount)
                    viewModel.dismissAllModals()
                }
            )
        }
    }

    Scaffold(
        topBar = {
            LiveSaleTopBar(
                title = uiState.event?.title ?: "Live Sale",
                onNavigateBack = onNavigateBack,
                actions = {
                    // Show end-sale button if event is ongoing
                    if (uiState.event?.status == EventStatus.LIVE) {
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
            // FAB visibility based on selected tab
            when (selectedTab) {
                LiveSaleTab.TRANSACTIONS -> AppFloatingActionButton(onClick = viewModel::showAddTransactionModal)
                LiveSaleTab.EXPENSES -> AppFloatingActionButton(onClick = viewModel::showAddExpenseModal)
                else -> {}
            }
        }
    ) { innerPadding ->
        // Show loading spinner if event isn't loaded yet
        if (uiState.event == null) {
            Box(Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            Column(modifier = Modifier.fillMaxSize().padding(innerPadding)) {

                // Displays summary stats
                LiveSaleStatsHeader(
                    totalItemsSold = uiState.totalItemsSold,
                    totalRevenueInCents = uiState.totalRevenueInCents,
                    totalExpensesInCents = uiState.totalExpensesInCents,
                    profitInCents = uiState.profitInCents
                )

                // Tab selection row
                TabRow(
                    selectedTabIndex = selectedTab.ordinal,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    LiveSaleTab.entries.forEach { tab ->
                        Tab(
                            selected = selectedTab == tab,
                            onClick = { selectedTab = tab },
                            text = { Text(tab.title) }
                        )
                    }
                }

                // Tab content container
                Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                    when (selectedTab) {
                        LiveSaleTab.TRANSACTIONS -> TransactionsContent(uiState.transactions)
                        LiveSaleTab.INVENTORY -> EventInventorySection(uiState.inventory, uiState.transactions)
                        LiveSaleTab.EXPENSES -> EventExpensesSection(uiState.expenses)
                    }
                }
            }
        }
    }
}

@Composable
private fun LiveSaleStatsHeader(
    totalItemsSold: Int,
    totalRevenueInCents: Long,
    totalExpensesInCents: Long,
    profitInCents: Long
) {
    // Summary box with revenue, expenses, etc.
    Card(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        border = BorderStroke(1.dp, Color.Gray.copy(alpha = 0.3f)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            StatCard(Modifier.weight(1f), "$totalItemsSold", "Sold")
            StatCard(Modifier.weight(1f), "₱${"%.0f".format(totalRevenueInCents / 100.0)}", "Revenue")
            StatCard(Modifier.weight(1f), "₱${"%.0f".format(totalExpensesInCents / 100.0)}", "Expenses")
            StatCard(Modifier.weight(1f), "₱${"%.0f".format(profitInCents / 100.0)}", "Profit")
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
        Text(text = value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = AlleyMainOrange)
        Text(text = label, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
    }
}

@Composable
private fun TransactionsContent(transactions: List<TransactionWithItems>) {
    if (transactions.isEmpty()) {
        // Shown if there are no transactions yet
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
            items(transactions, key = { it.transaction.transactionId }) {
                TransactionListItem(transactionWithItems = it)
            }
        }
    }
}

@Composable
private fun EventInventorySection(
    inventory: List<EventInventoryWithDetails>,
    transactions: List<TransactionWithItems>
) {
    // Pre-compute total quantity sold per item
    val soldItemsMap = remember(transactions) {
        transactions.flatMap { it.items }
            .groupBy { it.saleTransactionItem.itemId }
            .mapValues { entry -> entry.value.sumOf { it.saleTransactionItem.quantity } }
    }

    if (inventory.isEmpty()) {
        // Message when no inventory is assigned to the event
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
            shape = RoundedCornerShape(12.dp)
        ) {
            Box(Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                Text("No items allocated to this event", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
            }
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            items(inventory, key = { it.eventInventoryItem.itemId }) {
                val soldQuantity = soldItemsMap[it.catalogueItem.itemId] ?: 0
                AppCard {
                    InventoryItem(
                        name = it.catalogueItem.name,
                        price = it.catalogueItem.price,
                        allocated = it.eventInventoryItem.allocatedQuantity,
                        sold = soldQuantity
                    )
                }
            }
        }
    }
}

@Composable
private fun InventoryItem(name: String, price: Double, allocated: Int, sold: Int) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(Modifier.weight(1f)) {
            Text(name, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
            Text("Allocated: $allocated | Sold: $sold", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
        }
        Text("₱${"%.2f".format(price)}", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = AlleyMainOrange)
    }
}

@Composable
private fun EventExpensesSection(expenses: List<EventExpense>) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        if (expenses.isNotEmpty()) {
            // Expense summary card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = AlleyMainOrange.copy(alpha = 0.1f)),
                border = BorderStroke(1.dp, AlleyMainOrange.copy(alpha = 0.3f)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Total Expenses", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = AlleyMainOrange)
                    Text("₱${"%.2f".format(expenses.sumOf { it.amountInCents } / 100.0)}", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = AlleyMainOrange)
                }
            }
        }

        LazyColumn(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            if (expenses.isEmpty()) {
                // Message when no expenses are listed
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Box(Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                            Text("No expenses recorded", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
                        }
                    }
                }
            } else {
                // List individual expense items
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column {
                            expenses.forEachIndexed { index, expense ->
                                ExpenseItem(description = expense.description, amount = expense.amountInCents / 100.0)
                                if (index < expenses.lastIndex) {
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
        }
    }
}

@Composable
private fun ExpenseItem(description: String, amount: Double) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(description, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium, modifier = Modifier.weight(1f))
        Text("₱${"%.2f".format(amount)}", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = Color(0xFFF44336))
    }
}
