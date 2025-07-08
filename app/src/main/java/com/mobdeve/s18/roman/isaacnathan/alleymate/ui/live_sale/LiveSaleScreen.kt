package com.mobdeve.s18.roman.isaacnathan.alleymate.ui.live_sale

import android.app.Application
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext

import androidx.compose.ui.unit.dp
import com.mobdeve.s18.roman.isaacnathan.alleymate.common.components.LiveSaleTopBar
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.Transaction
import com.mobdeve.s18.roman.isaacnathan.alleymate.ui.live_sale.components.TransactionListItem

import androidx.compose.runtime.*

import androidx.lifecycle.viewmodel.compose.viewModel


private enum class LiveSaleTab(val title: String) {
    OVERVIEW("Overview"),
    INVENTORY("Inventory"),
    TRANSACTIONS("Transactions")
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
    val transactions by viewModel.transactions.collectAsState()

    var selectedTab by remember { mutableStateOf(LiveSaleTab.TRANSACTIONS) }



    Scaffold(
        topBar = {
            LiveSaleTopBar(
                title = event?.title ?: "Live Sale",
                onNavigateBack = onNavigateBack
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { /* TODO: Open Add Transaction Modal */ }) {
                Icon(Icons.Default.Add, contentDescription = "Add Transaction")
            }
        }
    ) { innerPadding ->

        Column(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            // TabRow for switching views
            TabRow(selectedTabIndex = selectedTab.ordinal) {
                LiveSaleTab.entries.forEach { tab ->
                    Tab(
                        selected = selectedTab == tab,
                        onClick = { selectedTab = tab },
                        text = { Text(tab.title) }
                    )
                }
            }

            // Content for the selected tab
            when (selectedTab) {
                LiveSaleTab.OVERVIEW -> {
                    // TODO: Reuse your EventOverviewSection from EventDetailScreen
                    // Pass it the 'event' state
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Overview Content Here")
                    }
                }

                LiveSaleTab.INVENTORY -> {
                    // TODO: Reuse your EventInventorySection from EventDetailScreen
                    // Pass it the 'inventory' state
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Inventory Content Here")
                    }
                }

                LiveSaleTab.TRANSACTIONS -> {
                    TransactionsContent(transactions = transactions)
                }
            }
        }
    }

}


@Composable
private fun TransactionsContent(transactions: List<Transaction>) {
    if (transactions.isEmpty()) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No transactions recorded yet.")
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(items = transactions, key = { it.id }) { transaction ->
                TransactionListItem(transaction = transaction)
            }
        }
    }
}