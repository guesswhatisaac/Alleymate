package com.mobdeve.s18.roman.isaacnathan.alleymate.ui.events

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.local.AlleyMateDatabase
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.Event
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.repository.EventRepository
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

import kotlinx.coroutines.launch

class EventViewModel(application: Application) : AndroidViewModel(application) {

    private val eventRepository: EventRepository

    val allEvents: StateFlow<List<EventUiModel>>

    init {
        val database = AlleyMateDatabase.getDatabase(application)
        eventRepository = EventRepository(database.eventDao(), database.catalogueDao(), database.transactionDao())

        val eventsFlow = eventRepository.getHydratedEvents()
        allEvents = eventsFlow.stateIn(
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