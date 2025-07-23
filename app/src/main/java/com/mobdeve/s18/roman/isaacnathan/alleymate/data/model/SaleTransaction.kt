package com.mobdeve.s18.roman.isaacnathan.alleymate.data.model

import androidx.room.*

/**
 * Represents a single transaction that took place during an event.
 * Automatically deletes the transaction if the parent event is deleted.
 */
@Entity(
    tableName = "transactions",
    foreignKeys = [ForeignKey(entity = Event::class, parentColumns = ["eventId"], childColumns = ["eventId"], onDelete = ForeignKey.CASCADE)],
    indices = [Index("eventId")]
)
data class SaleTransaction(
    @PrimaryKey(autoGenerate = true)
    val transactionId: Int = 0,
    val eventId: Int,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "transaction_items",
    primaryKeys = ["transactionId", "itemId"]
)
data class SaleTransactionItem(
    val transactionId: Int,
    val itemId: Int,
    val quantity: Int,
    val priceInCents: Long
)

