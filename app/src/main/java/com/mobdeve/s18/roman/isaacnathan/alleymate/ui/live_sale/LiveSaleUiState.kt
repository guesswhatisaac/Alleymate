package com.mobdeve.s18.roman.isaacnathan.alleymate.ui.live_sale

import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.Event
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.EventExpense
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.relations.EventInventoryWithDetails
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.relations.TransactionWithItems

data class LiveSaleUiState(
    val event: Event? = null,
    val inventory: List<EventInventoryWithDetails> = emptyList(),
    val expenses: List<EventExpense> = emptyList(),
    val transactions: List<TransactionWithItems> = emptyList(),

    val totalItemsSold: Int = 0,
    val totalRevenueInCents: Long = 0,
    val totalExpensesInCents: Long = 0,
    val profitInCents: Long = 0,
    val totalStockLeft: Int = 0
)