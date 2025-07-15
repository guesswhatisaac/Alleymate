package com.mobdeve.s18.roman.isaacnathan.alleymate.data.repository

import com.mobdeve.s18.roman.isaacnathan.alleymate.data.local.CatalogueDao
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.local.EventDao
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.local.TransactionDao
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.CatalogueItem
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.Event
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.EventExpense
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.EventInventoryItem
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.SaleTransaction
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.SaleTransactionItem
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.relations.EventInventoryWithDetails
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import com.mobdeve.s18.roman.isaacnathan.alleymate.ui.events.EventUiModel
import kotlinx.coroutines.flow.map

class EventRepository(
    private val eventDao: EventDao,
    private val catalogueDao: CatalogueDao,
    private val transactionDao: TransactionDao

) {

    fun getAllEvents(): Flow<List<Event>> {
        return eventDao.getAllEvents()
    }

    suspend fun addEvent(event: Event) {
        eventDao.insertEvent(event)
    }

    suspend fun updateEvent(event: Event) {
        eventDao.updateEvent(event)
    }

    fun getInventoryForEvent(eventId: Int): Flow<List<EventInventoryWithDetails>> {
        return eventDao.getInventoryForEvent(eventId)
    }

    fun getEventById(eventId: Int): Flow<Event?> {
        return eventDao.getEventById(eventId)
    }

    fun getExpensesForEvent(eventId: Int): Flow<List<EventExpense>> {
        return eventDao.getExpensesForEvent(eventId)
    }

    suspend fun insertExpense(expense: EventExpense) {
        eventDao.insertExpense(expense)
    }

    suspend fun stackAllocateItemsToEvent(eventId: Int, itemsToAllocate: Map<Int, Int>) {
        val updatedInventory = mutableListOf<EventInventoryItem>()

        for ((itemId, quantity) in itemsToAllocate) {
            if (quantity <= 0) continue

            val existingItem = eventDao.getInventoryItem(eventId, itemId)

            val newTotalAllocatedQuantity = if (existingItem != null) {
                existingItem.allocatedQuantity + quantity
            } else {
                quantity
            }

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

    suspend fun deleteEvent(event: Event) {
        eventDao.deleteEvent(event)
    }

    fun getHydratedEvents(): Flow<List<EventUiModel>> {
        return eventDao.getEventSummaries()
            .map { summaries ->
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

    fun getLiveEvents(): Flow<List<Event>> {
        return eventDao.getLiveEvents()
    }


    fun getTransactionsForEvent(eventId: Int): Flow<List<com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.relations.TransactionWithItems>> {
        return transactionDao.getTransactionsForEvent(eventId)
    }

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

}