package com.mobdeve.s18.roman.isaacnathan.alleymate.data.model

data class TransactionItem(
    val id: Int,
    val name: String,
    val category: String,
    val price: Int,
    val quantity: Int
)

data class Transaction(
    val id: Int,
    val time: String,
    val items: List<TransactionItem>
) {
    val totalQuantity: Int
        get() = items.sumOf { it.quantity }

    val totalPrice: Int
        get() = items.sumOf { it.price * it.quantity }
}