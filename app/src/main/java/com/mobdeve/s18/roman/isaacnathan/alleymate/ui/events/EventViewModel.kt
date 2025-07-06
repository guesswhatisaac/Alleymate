package com.mobdeve.s18.roman.isaacnathan.alleymate.ui.events

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.local.AlleyMateDatabase
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.Event
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.repository.EventRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.map

class EventViewModel(application: Application) : AndroidViewModel(application) {

    private val eventRepository: EventRepository

    val allEvents: StateFlow<List<Event>>

    init {
        val eventDao = AlleyMateDatabase.getDatabase(application).eventDao()
        val catalogueDao = AlleyMateDatabase.getDatabase(application).catalogueDao()

        eventRepository = EventRepository(eventDao, catalogueDao)

        allEvents = eventRepository.getAllEventsWithDetails()
            .map { listOfEventsWithDetails ->
                listOfEventsWithDetails.map { eventWithDetails ->
                    val event = eventWithDetails.event
                    val inventory = eventWithDetails.inventory
                    val expenses = eventWithDetails.expenses

                    event.apply {
                        totalItemsAllocated = inventory.sumOf { it.eventInventoryItem.allocatedQuantity }
                        totalItemsSold = inventory.sumOf { it.eventInventoryItem.soldQuantity }
                        totalExpensesInCents = expenses.sumOf { it.amountInCents }
                        totalRevenueInCents = inventory.sumOf {
                            it.eventInventoryItem.soldQuantity * (it.catalogueItem.price * 100).toLong()
                        }
                    }
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000L),
                initialValue = emptyList()
            )
    }

    fun addEvent(title: String, location: String, startDate: Long, endDate: Long) = viewModelScope.launch {
        val newEvent = Event(
            title = title,
            location = location,
            startDate = startDate,
            endDate = endDate
        )
        eventRepository.addEvent(newEvent)
    }

    fun updateEvent(event: Event) = viewModelScope.launch {
        eventRepository.updateEvent(event)
    }


}