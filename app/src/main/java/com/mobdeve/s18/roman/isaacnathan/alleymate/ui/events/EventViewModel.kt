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

    private val repository: EventRepository

    val allEvents: StateFlow<List<Event>>

    init {
        val eventDao = AlleyMateDatabase.getDatabase(application).eventDao()
        repository = EventRepository(eventDao)

        // We transform the Flow<List<Event>> into another Flow<List<Event>>
        allEvents = repository.getAllEvents()
            .map { eventsFromDb ->
                // For each Event from the database...
                eventsFromDb.map { event ->
                    // ...we "hydrate" it by setting its @Ignore fields.
                    // TODO: Replace these hardcoded stats with real data from new queries.
                    event.apply {
                        totalItemsAllocated = 100 // Placeholder
                        totalItemsSold = 50      // Placeholder
                        totalExpensesInCents = 250000 // Placeholder
                        totalRevenueInCents = 750000   // Placeholder
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
        repository.addEvent(newEvent)
    }

    fun updateEvent(event: Event) = viewModelScope.launch {
        repository.updateEvent(event)
    }


}