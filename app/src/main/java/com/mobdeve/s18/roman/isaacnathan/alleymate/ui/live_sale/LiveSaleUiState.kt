package com.mobdeve.s18.roman.isaacnathan.alleymate.ui.live_sale

import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.Event
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.EventExpense
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.relations.EventInventoryWithDetails
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.relations.TransactionWithItems

// UI state container for the Live Sale screen
data class LiveSaleUiState(
    val event: Event? = null, // Current live sale event, if any
    val inventory: List<EventInventoryWithDetails> = emptyList(), // Inventory tied to the event
    val expenses: List<EventExpense> = emptyList(), // Expenses recorded during the event
    val transactions: List<TransactionWithItems> = emptyList(), // Transactions completed

    // Derived statistics
    val totalItemsSold: Int = 0,
    val totalRevenueInCents: Long = 0,
    val totalExpensesInCents: Long = 0,
    val profitInCents: Long = 0,
    val totalStockLeft: Int = 0
)
