package com.mobdeve.s18.roman.isaacnathan.alleymate.ui.events

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.local.AlleyMateDatabase
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.Event
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.repository.EventRepository
import kotlinx.coroutines.flow.Flow

import kotlinx.coroutines.launch

class EventViewModel(application: Application) : AndroidViewModel(application) {

    private val eventRepository: EventRepository

    val allEventsFlow: Flow<List<Event>>

    init {
        val database = AlleyMateDatabase.getDatabase(application)
        eventRepository = EventRepository(database.eventDao(), database.catalogueDao())

        allEventsFlow = eventRepository.getHydratedEvents()
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