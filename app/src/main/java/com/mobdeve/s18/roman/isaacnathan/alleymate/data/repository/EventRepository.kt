package com.mobdeve.s18.roman.isaacnathan.alleymate.data.repository

import com.mobdeve.s18.roman.isaacnathan.alleymate.data.local.EventDao
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.Event
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.EventExpense
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.relations.EventInventoryWithDetails
import kotlinx.coroutines.flow.Flow

class EventRepository(private val eventDao: EventDao) {

    // --- Existing Functions ---
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
}