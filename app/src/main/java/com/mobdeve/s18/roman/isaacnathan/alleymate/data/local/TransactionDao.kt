package com.mobdeve.s18.roman.isaacnathan.alleymate.data.local

import androidx.room.*
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.*
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.query_results.MonthlyRevenue
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.query_results.SalesData
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

    @Query("""
            SELECT
                ti.itemId,
                ci.name, -- THIS WAS THE MISSING PIECE
                SUM(ti.quantity) as quantitySold,
                SUM(ti.quantity * ti.priceInCents) as totalRevenueInCents
            FROM transaction_items ti
            JOIN transactions t ON ti.transactionId = t.transactionId
            JOIN catalogue_items ci ON ti.itemId = ci.itemId
            WHERE t.eventId IN (:eventIds) AND t.timestamp >= :startDate
            GROUP BY ti.itemId, ci.name
            ORDER BY quantitySold DESC
        """)
    fun getSalesData(eventIds: List<Int>, startDate: Long): Flow<List<SalesData>>

    @Query("""
        SELECT
            CAST(strftime('%Y', timestamp / 1000, 'unixepoch') AS INTEGER) as year,
            CAST(strftime('%m', timestamp / 1000, 'unixepoch') AS INTEGER) as month,
            SUM(ti.quantity * ti.priceInCents) as totalRevenueInCents
        FROM transactions t
        JOIN transaction_items ti ON t.transactionId = ti.transactionId
        WHERE t.timestamp >= :startTime
        GROUP BY year, month
        ORDER BY year, month ASC
    """)
    fun getMonthlyRevenue(startTime: Long): Flow<List<MonthlyRevenue>>


}