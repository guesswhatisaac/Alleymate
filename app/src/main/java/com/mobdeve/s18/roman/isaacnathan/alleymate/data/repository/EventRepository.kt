package com.mobdeve.s18.roman.isaacnathan.alleymate.data.repository

import com.mobdeve.s18.roman.isaacnathan.alleymate.data.local.CatalogueDao
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.local.EventDao
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.Event
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.EventExpense
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.EventInventoryItem
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.relations.EventInventoryWithDetails
import kotlinx.coroutines.flow.Flow
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.relations.EventWithDetails

class EventRepository(
    private val eventDao: EventDao,
    private val catalogueDao: CatalogueDao
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

    fun getAllEventsWithDetails(): Flow<List<EventWithDetails>> {
        return eventDao.getAllEventsWithDetails()
    }

    suspend fun allocateItemsToEvent(inventory: List<EventInventoryItem>) {
        eventDao.upsertEventInventory(inventory)
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

    suspend fun reduceCatalogueStock(itemId: Int, quantityToReduce: Int) {
        catalogueDao.reduceStock(itemId, quantityToReduce)
    }


}