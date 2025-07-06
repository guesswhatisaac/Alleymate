package com.mobdeve.s18.roman.isaacnathan.alleymate.ui.allocate

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.AllocationStateHolder
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.local.AlleyMateDatabase
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.CatalogueItem
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.Event
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.EventInventoryItem
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.repository.EventRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AllocationViewModel(application: Application) : AndroidViewModel(application) {

    private val eventRepository: EventRepository


    val allEvents = MutableStateFlow<List<Event>>(emptyList())
    val selectedEvent = MutableStateFlow<Event?>(null)

    val itemsToAllocate: StateFlow<List<CatalogueItem>> = AllocationStateHolder.itemsToAllocate
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _allocationQuantities = MutableStateFlow<Map<Int, Int>>(emptyMap())
    val allocationQuantities = _allocationQuantities.asStateFlow()

    init {

        val eventDao = AlleyMateDatabase.getDatabase(application).eventDao()
        val catalogueDao = AlleyMateDatabase.getDatabase(application).catalogueDao()
        eventRepository = EventRepository(eventDao, catalogueDao)

        viewModelScope.launch {
            val events = eventRepository.getAllEvents().first()
            allEvents.value = events
            if (events.isNotEmpty()) {
                selectedEvent.value = events[0]
            }
        }
    }

    // --- USER ACTIONS ---

    fun selectEvent(event: Event) {
        selectedEvent.value = event
    }

    fun updateAllocationQuantity(itemId: Int, quantity: Int) {
        _allocationQuantities.value = _allocationQuantities.value.toMutableMap().apply {
            this[itemId] = quantity
        }
    }

    fun removeAllocationItem(itemId: Int) {
        AllocationStateHolder.removeItem(itemId)
    }

    fun performAllocation(onSuccess: () -> Unit) {
        viewModelScope.launch {
            val event = selectedEvent.value ?: return@launch
            val quantities = _allocationQuantities.value

            eventRepository.stackAllocateItemsToEvent(event.eventId, quantities)

            val inventoryToSave = itemsToAllocate.value.mapNotNull { catalogueItem ->
                val quantity = quantities[catalogueItem.itemId] ?: 0
                if (quantity > 0) {
                    EventInventoryItem(
                        eventId = event.eventId,
                        itemId = catalogueItem.itemId,
                        allocatedQuantity = quantity
                    )
                } else {
                    null
                }
            }

            if (inventoryToSave.isNotEmpty()) {
                eventRepository.allocateItemsToEvent(inventoryToSave)
            }

            AllocationStateHolder.clear()
            onSuccess()
        }
    }
}