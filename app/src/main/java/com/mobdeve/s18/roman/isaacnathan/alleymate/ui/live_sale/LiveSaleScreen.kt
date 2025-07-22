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

    // --- Collect the single UI state object ---
    val uiState by viewModel.uiState.collectAsState()

    // --- Collect state for modals and transactions ---
    val modalState by viewModel.modalState.collectAsState()
    val addTransactionState by viewModel.addTransactionState.collectAsState()
    val selectedItemsForTransaction by viewModel.selectedItemsForTransaction.collectAsState()
    val transactionCart by viewModel.transactionCart.collectAsState()

    var selectedTab by remember { mutableStateOf(LiveSaleTab.TRANSACTIONS) }
    var showEndEventDialog by remember { mutableStateOf(false) }

    // End Event Confirmation Dialog
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

    // Modal Handling
    when (modalState) {
        LiveSaleModalState.Hidden -> { /* Do nothing */ }
        LiveSaleModalState.AddTransaction -> {
            if (addTransactionState != AddTransactionState.Hidden) {
                AddTransactionModal(
                    inventory = uiState.inventory, // Pass data from uiState
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
                title = uiState.event?.title ?: "Live Sale", // Read from uiState
                onNavigateBack = onNavigateBack,
                actions = {
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
            when (selectedTab) {
                LiveSaleTab.TRANSACTIONS -> {
                    AppFloatingActionButton(onClick = viewModel::showAddTransactionModal)
                }
                LiveSaleTab.EXPENSES -> {
                    AppFloatingActionButton(onClick = viewModel::showAddExpenseModal)
                }
                else -> { }
            }
        }
    ) { innerPadding ->
        if (uiState.event == null) {
            Box(Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            Column(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
                // Pass the calculated values directly from uiState to the header
                LiveSaleStatsHeader(
                    totalItemsSold = uiState.totalItemsSold,
                    totalRevenueInCents = uiState.totalRevenueInCents,
                    totalExpensesInCents = uiState.totalExpensesInCents,
                    profitInCents = uiState.profitInCents
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
                            text = { Text(tab.title) }
                        )
                    }
                }

                // Tab Content with consistent padding
                Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                    when (selectedTab) {
                        LiveSaleTab.TRANSACTIONS -> {
                            TransactionsContent(transactions = uiState.transactions)
                        }
                        LiveSaleTab.INVENTORY -> {
                            EventInventorySection(inventory = uiState.inventory, transactions = uiState.transactions)
                        }
                        LiveSaleTab.EXPENSES -> {
                            EventExpensesSection(expenses = uiState.expenses)
                        }
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
    Card(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        border = BorderStroke(1.dp, Color.Gray.copy(alpha = 0.3f)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            StatCard(modifier = Modifier.weight(1f), value = "$totalItemsSold", label = "Sold")
            StatCard(modifier = Modifier.weight(1f), value = "₱${"%.0f".format(totalRevenueInCents / 100.0)}", label = "Revenue")
            StatCard(modifier = Modifier.weight(1f), value = "₱${"%.0f".format(totalExpensesInCents / 100.0)}", label = "Expenses")
            StatCard(modifier = Modifier.weight(1f), value = "₱${"%.0f".format(profitInCents / 100.0)}", label = "Profit")
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
private fun EventInventorySection(
    inventory: List<EventInventoryWithDetails>,
    transactions: List<TransactionWithItems>
) {
    val soldItemsMap = remember(transactions) {
        transactions.flatMap { it.items }
            .groupBy { it.saleTransactionItem.itemId }
            .mapValues { entry -> entry.value.sumOf { it.saleTransactionItem.quantity } }
    }

    if (inventory.isEmpty()) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
            shape = RoundedCornerShape(12.dp)
        ) {
            Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                Text(
                    text = "No items allocated to this event",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            items(
                items = inventory,
                key = { it.eventInventoryItem.itemId }
            ) { inventoryItemWithDetails ->
                val catalogueItem = inventoryItemWithDetails.catalogueItem
                val eventInventory = inventoryItemWithDetails.eventInventoryItem
                val soldQuantity = soldItemsMap[catalogueItem.itemId] ?: 0

                AppCard {
                    InventoryItem(
                        name = catalogueItem.name,
                        price = catalogueItem.price,
                        allocated = eventInventory.allocatedQuantity,
                        sold = soldQuantity
                    )
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
        modifier = Modifier.fillMaxWidth().padding(16.dp),
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
private fun EventExpensesSection(
    expenses: List<EventExpense>,
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        if (expenses.isNotEmpty()) {
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

        LazyColumn(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            if (expenses.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                            Text(
                                text = "No expenses recorded",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Gray
                            )
                        }
                    }
                }
            } else {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
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
        modifier = Modifier.fillMaxWidth().padding(16.dp),
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