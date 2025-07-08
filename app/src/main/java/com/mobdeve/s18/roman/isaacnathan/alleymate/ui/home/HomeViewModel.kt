package com.mobdeve.s18.roman.isaacnathan.alleymate.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.local.AlleyMateDatabase
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.repository.CatalogueRepository
import kotlinx.coroutines.launch
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.Event
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.EventStatus
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.repository.EventRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val catalogueRepository: CatalogueRepository
    private val eventRepository: EventRepository

    val liveEvent: StateFlow<Event?>
    val upcomingEvents: StateFlow<List<Event>>

    init {
        val database = AlleyMateDatabase.getDatabase(application)
        eventRepository = EventRepository(database.eventDao(), database.catalogueDao())
        val allHydratedEventsFlow = eventRepository.getHydratedEvents()

        liveEvent = allHydratedEventsFlow
            .map { allEvents ->
                allEvents.firstOrNull { it.status == EventStatus.LIVE }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000L),
                initialValue = null
            )


        upcomingEvents = allHydratedEventsFlow
            .map { allEvents ->
                allEvents.filter { it.status == EventStatus.UPCOMING }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000L),
                initialValue = emptyList() // Start with an empty list
            )    }

    init {
        val database = AlleyMateDatabase.getDatabase(application)
        catalogueRepository = CatalogueRepository(database.catalogueDao(), database)
    }

    fun deleteAllCatalogueItems() {
        viewModelScope.launch {
            catalogueRepository.deleteAllItems()
        }
    }

    fun restartDatabase() {
        viewModelScope.launch {
            catalogueRepository.clearEntireDatabase()
        }
    }

}