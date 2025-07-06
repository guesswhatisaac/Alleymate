package com.mobdeve.s18.roman.isaacnathan.alleymate.ui.events

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mobdeve.s18.roman.isaacnathan.alleymate.common.components.AppTopBar
import com.mobdeve.s18.roman.isaacnathan.alleymate.common.components.SectionHeader
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.local.AlleyMateDatabase
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.Event
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.EventExpense
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.relations.EventInventoryWithDetails
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.repository.EventRepository

@Composable
fun EventDetailScreen(
    eventId: Int,
    onNavigateBack: () -> Unit
) {
    // --- Get Dependencies and ViewModel ---
    val context = LocalContext.current
    val eventRepository = remember { EventRepository(AlleyMateDatabase.getDatabase(context).eventDao()) }
    val viewModel: EventDetailViewModel = viewModel(
        factory = EventDetailViewModelFactory(eventRepository, eventId)
    )

    // --- Collect State from ViewModel ---
    val event by viewModel.event.collectAsState()
    val inventory by viewModel.inventory.collectAsState()
    val expenses by viewModel.expenses.collectAsState()

    Scaffold(
        topBar = {
            AppTopBar(
                title = event?.title ?: "Event Details",
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Navigate back")
                    }
                }
            )
        }
    ) { innerPadding ->
        val currentEvent = event
        if (currentEvent == null) {
            // Show a loading indicator while the main event data is being fetched
            Box(
                Modifier.fillMaxSize().padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            // Content is ready, display it in a LazyColumn
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // Top Header Section with stats
                item { EventDetailHeader(event = currentEvent) }

                // Inventory Section
                item { EventInventorySection(inventory = inventory) }

                // Expenses Section
                item {
                    EventExpensesSection(
                        expenses = expenses,
                        onAddExpense = viewModel::addExpense
                    )
                }
            }
        }
    }
}

// ====================================================================================
//  PRIVATE HELPER COMPOSABLES - Specific to EventDetailScreen
// ====================================================================================

@Composable
private fun EventDetailHeader(event: Event) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text(event.dateRangeString, style = MaterialTheme.typography.labelMedium, color = Color.Gray)
        Text(event.location, style = MaterialTheme.typography.labelMedium, color = Color.Gray)
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            StatColumn(value = "${event.totalItemsSold}", label = "Sold")
            StatColumn(value = "₱${"%.2f".format(event.totalRevenueInCents / 100.0)}", label = "Revenue")
            StatColumn(value = "₱${"%.2f".format(event.totalExpensesInCents / 100.0)}", label = "Expenses")
            StatColumn(value = "₱${"%.2f".format(event.profitInCents / 100.0)}", label = "Profit")
        }
    }
}

@Composable
private fun StatColumn(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Text(label, style = MaterialTheme.typography.labelMedium, color = Color.Gray)
    }
}

@Composable
private fun EventInventorySection(
    inventory: List<EventInventoryWithDetails>
) {
    Column {
        SectionHeader(
            title = "Inventory",
            modifier = Modifier.padding(horizontal = 16.dp),
            showDivider = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (inventory.isEmpty()) {
            Text("No items allocated.", modifier = Modifier.padding(16.dp), color = Color.Gray)
        } else {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                inventory.forEach { inventoryItemWithDetails  ->

                    val catalogueItem = inventoryItemWithDetails.catalogueItem
                    val eventInventory = inventoryItemWithDetails.eventInventoryItem

                    ListItem(
                        headlineContent = { Text(catalogueItem.name) },
                        supportingContent = {
                            Text("Allocated: ${eventInventory.allocatedQuantity} | Sold: ${eventInventory.soldQuantity}")
                        },
                        trailingContent = {
                            Text("₱${"%.2f".format(catalogueItem.price)}")
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun EventExpensesSection(
    expenses: List<EventExpense>,
    onAddExpense: (description: String, amount: Double) -> Unit
) {
    var expenseDesc by remember { mutableStateOf("") }
    var expenseAmount by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(bottom = 16.dp)) {
        SectionHeader(
            title = "Expenses",
            modifier = Modifier.padding(horizontal = 16.dp),
            showDivider = true
        )
        if (expenses.isEmpty()) {
            Text("No expenses recorded.", modifier = Modifier.padding(16.dp), color = Color.Gray)
        } else {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                expenses.forEach { expense ->
                    ListItem(
                        headlineContent = { Text(expense.description) },
                        trailingContent = {
                            Text("₱${"%.2f".format(expense.amountInCents / 100.0)}")
                        }
                    )
                }
            }
        }

        HorizontalDivider(modifier = Modifier.padding(top = 8.dp, bottom = 16.dp))

        Column(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Add New Expense", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
            OutlinedTextField(
                value = expenseDesc,
                onValueChange = { expenseDesc = it },
                label = { Text("Expense Description") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = expenseAmount,
                onValueChange = { expenseAmount = it },
                label = { Text("Amount") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            Button(
                onClick = {
                    onAddExpense(expenseDesc, expenseAmount.toDoubleOrNull() ?: 0.0)
                    expenseDesc = ""
                    expenseAmount = ""
                },
                enabled = expenseDesc.isNotBlank() && (expenseAmount.toDoubleOrNull() ?: 0.0) > 0,
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Add Expense")
            }
        }
    }
}