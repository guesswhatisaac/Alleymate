package com.mobdeve.s18.roman.isaacnathan.alleymate.data.repository

import androidx.room.Transaction
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.local.CatalogueDao
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.local.EventDao
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.local.TransactionDao
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.*
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.relations.EventInventoryWithDetails
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.relations.TransactionWithItems
import com.mobdeve.s18.roman.isaacnathan.alleymate.ui.events.EventUiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

/**
 * Repository for managing event-related operations including inventory,
 * expenses, sales transactions, and event lifecycle handling.
 */
class EventRepository(
    private val eventDao: EventDao,
    private val catalogueDao: CatalogueDao,
    private val transactionDao: TransactionDao
) {

    /** Returns all events from the database. */
    fun getAllEvents(): Flow<List<Event>> = eventDao.getAllEvents()

    /** Returns live events only. */
    fun getLiveEvents(): Flow<List<Event>> = eventDao.getLiveEvents()

    /** Returns events that are either live or upcoming. */
    fun getActiveEvents(): Flow<List<Event>> = eventDao.getActiveEvents()

    /** Returns a specific event by ID. */
    fun getEventById(eventId: Int): Flow<Event?> = eventDao.getEventById(eventId)

    /** Inserts a new event into the database. */
    suspend fun addEvent(event: Event) {
        eventDao.insertEvent(event)
    }

    /** Updates an existing event. */
    suspend fun updateEvent(event: Event) {
        eventDao.updateEvent(event)
    }

    /** Deletes an event and its related data. */
    suspend fun deleteEvent(event: Event) {
        eventDao.deleteEvent(event)
    }

    /** Returns the inventory (with item details) for a given event. */
    fun getInventoryForEvent(eventId: Int): Flow<List<EventInventoryWithDetails>> =
        eventDao.getInventoryForEvent(eventId)

    /**
     * Allocates stock to an event. If an inventory item exists, adds to the allocated quantity.
     * Reduces the general catalogue stock accordingly.
     */
    suspend fun stackAllocateItemsToEvent(eventId: Int, itemsToAllocate: Map<Int, Int>) {
        val updatedInventory = mutableListOf<EventInventoryItem>()

        for ((itemId, quantity) in itemsToAllocate) {
            if (quantity <= 0) continue

            val existingItem = eventDao.getInventoryItem(eventId, itemId)
            val newTotalAllocatedQuantity = existingItem?.allocatedQuantity?.plus(quantity) ?: quantity

            updatedInventory.add(
                EventInventoryItem(
                    eventId = eventId,
                    itemId = itemId,
                    allocatedQuantity = newTotalAllocatedQuantity,
                    soldQuantity = existingItem?.soldQuantity ?: 0
                )
            )

            catalogueDao.reduceStock(itemId, quantity)
        }

        if (updatedInventory.isNotEmpty()) {
            eventDao.upsertEventInventory(updatedInventory)
        }
    }

    /** Returns all expenses recorded for a given event. */
    fun getExpensesForEvent(eventId: Int): Flow<List<EventExpense>> =
        eventDao.getExpensesForEvent(eventId)

    /** Inserts a new expense for an event. */
    suspend fun insertExpense(expense: EventExpense) {
        eventDao.insertExpense(expense)
    }

    /** Returns all transactions made for a given event. */
    fun getTransactionsForEvent(eventId: Int): Flow<List<TransactionWithItems>> =
        transactionDao.getTransactionsForEvent(eventId)

    /**
     * Records a new sale transaction and updates sold quantities in event inventory.
     */
    suspend fun recordSaleTransaction(eventId: Int, cart: Map<Int, CatalogueItem>, quantities: Map<Int, Int>) {
        val newTransaction = SaleTransaction(eventId = eventId)
        val transactionId = transactionDao.insertTransaction(newTransaction)

        val transactionItems = quantities.map { (itemId, quantity) ->
            val itemPrice = cart[itemId]?.price ?: 0.0
            SaleTransactionItem(
                transactionId = transactionId.toInt(),
                itemId = itemId,
                quantity = quantity,
                priceInCents = (itemPrice * 100).toLong()
            )
        }

        transactionDao.insertTransactionItems(transactionItems)

        for ((itemId, quantity) in quantities) {
            val existingInventory = eventDao.getInventoryItem(eventId, itemId)
            if (existingInventory != null) {
                val updatedInventory = existingInventory.copy(
                    soldQuantity = existingInventory.soldQuantity + quantity
                )
                eventDao.upsertEventInventory(listOf(updatedInventory))
            }
        }
    }

    /**
     * Ends a sale by returning unsold inventory to the catalogue and marking the event as ENDED.
     */
    @Transaction
    suspend fun endSaleAndReturnStock(eventId: Int) {
        val inventoryForEvent = eventDao.getInventoryForEvent(eventId).first()

        for (inventoryItemWithDetails in inventoryForEvent) {
            val inventoryItem = inventoryItemWithDetails.eventInventoryItem
            val unsoldQuantity = inventoryItem.allocatedQuantity - inventoryItem.soldQuantity
            if (unsoldQuantity > 0) {
                catalogueDao.returnStock(inventoryItem.itemId, unsoldQuantity)
            }
        }

        val eventToUpdate = eventDao.getEventById(eventId).first()
        eventToUpdate?.let { event ->
            val updatedEvent = event.copy(status = EventStatus.ENDED)
            eventDao.updateEvent(updatedEvent)
        }
    }

    /**
     * Returns a UI-friendly list of event summaries, including financial and inventory data.
     */
    fun getHydratedEvents(): Flow<List<EventUiModel>> {
        return eventDao.getEventSummaries().map { summaries ->
            summaries.map { summary ->
                EventUiModel(
                    eventId = summary.eventId,
                    title = summary.title,
                    location = summary.location,
                    startDate = summary.startDate,
                    endDate = summary.endDate,
                    status = summary.status,
                    totalItemsAllocated = summary.totalItemsAllocated,
                    totalItemsSold = summary.totalItemsSold,
                    totalRevenueInCents = summary.totalRevenueInCents,
                    totalExpensesInCents = summary.totalExpensesInCents,
                    catalogueCount = summary.catalogueCount,
                    totalStockLeft = summary.totalItemsAllocated - summary.totalItemsSold
                )
            }
        }
    }
}
