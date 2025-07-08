package com.mobdeve.s18.roman.isaacnathan.alleymate.data.local

import androidx.room.*
import androidx.room.Transaction
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.*
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.relations.TransactionWithItems
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Insert
    suspend fun insertTransaction(transaction: SaleTransaction): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransactionItems(items: List<SaleTransactionItem>)

    @Transaction
    @Query("SELECT * FROM transactions WHERE eventId = :eventId ORDER BY timestamp DESC")
    fun getTransactionsForEvent(eventId: Int): Flow<List<TransactionWithItems>>
}