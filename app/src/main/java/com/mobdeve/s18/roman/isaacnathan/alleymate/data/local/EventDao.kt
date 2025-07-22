package com.mobdeve.s18.roman.isaacnathan.alleymate.data.local

import androidx.room.*
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.Event
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.EventExpense
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.EventInventoryItem
import kotlinx.coroutines.flow.Flow
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.relations.EventInventoryWithDetails
import androidx.room.Transaction
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.views.EventSummaryView


@Dao
interface EventDao {
    @Insert
    suspend fun insertEvent(event: Event)

    @Update
    suspend fun updateEvent(event: Event)

    @Query("SELECT * FROM events ORDER BY startDate DESC")
    fun getAllEvents(): Flow<List<Event>>

    @Insert
    suspend fun insertExpense(expense: EventExpense)

    @Query("SELECT * FROM event_expenses WHERE eventId = :eventId")
    fun getExpensesForEvent(eventId: Int): Flow<List<EventExpense>>

    @Query("SELECT * FROM event_expenses")
    fun getAllExpenses(): Flow<List<EventExpense>>

    @Query("SELECT SUM(amountInCents) FROM event_expenses WHERE eventId = :eventId")
    fun getTotalExpensesForEvent(eventId: Int): Flow<Long?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun allocateItemToEvent(inventoryItem: EventInventoryItem)

    @Query("DELETE FROM event_inventory WHERE eventId = :eventId AND itemId = :itemId")
    suspend fun deallocateItemFromEvent(eventId: Int, itemId: Int)

    @Transaction
    @Query("SELECT * FROM event_inventory WHERE eventId = :eventId")
    fun getInventoryForEvent(eventId: Int): Flow<List<EventInventoryWithDetails>>

    @Query("SELECT * FROM event_inventory WHERE eventId = :eventId AND itemId = :itemId")
    suspend fun getInventoryItem(eventId: Int, itemId: Int): EventInventoryItem?

    @Query("SELECT * FROM events WHERE eventId = :eventId")
    fun getEventById(eventId: Int): Flow<Event?>

    @Query("SELECT * FROM EventSummaryView ORDER BY startDate DESC")
    fun getEventSummaries(): Flow<List<EventSummaryView>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertEventInventory(inventory: List<EventInventoryItem>)

    @Delete
    suspend fun deleteEvent(event: Event)

    @Transaction
    @Query("SELECT * FROM event_inventory")
    fun getAllInventoryWithDetails(): Flow<List<EventInventoryWithDetails>>

    @Query("SELECT * FROM events WHERE status = 'LIVE'")
    fun getLiveEvents(): Flow<List<Event>>

    @Query("SELECT * FROM events WHERE status != 'ENDED' ORDER BY startDate DESC")
    fun getActiveEvents(): Flow<List<Event>>

    @Query("""
        SELECT SUM(amountInCents)
        FROM event_expenses
        WHERE eventId IN (:eventIds)
    """)
    fun getTotalExpensesForEvents(eventIds: List<Int>): Flow<Long?>



}