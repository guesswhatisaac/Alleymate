package com.mobdeve.s18.roman.isaacnathan.alleymate.ui.allocate

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.AllocationStateHolder
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.local.AlleyMateDatabase
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.CatalogueItem
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.Event
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.repository.CatalogueRepository
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.repository.EventRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class AllocationViewModel(application: Application) : AndroidViewModel(application) {

    private val eventRepository: EventRepository
    private val catalogueRepository: CatalogueRepository

    val allEvents = MutableStateFlow<List<Event>>(emptyList())
    val selectedEvent = MutableStateFlow<Event?>(null)

    // Reactive list of catalogue items to allocate based on selected item IDs
    val itemsToAllocate: StateFlow<List<CatalogueItem>>

    // Map of itemId to quantity input by user
    private val _allocationQuantities = MutableStateFlow<Map<Int, Int>>(emptyMap())
    val allocationQuantities = _allocationQuantities.asStateFlow()

    // True if all items have a non-zero quantity and at least one item exists
    val isAllocationValid: StateFlow<Boolean>

    init {
        val database = AlleyMateDatabase.getDatabase(application)
        catalogueRepository = CatalogueRepository(database.catalogueDao())
        eventRepository = EventRepository(
            database.eventDao(),
            database.catalogueDao(),
            database.transactionDao()
        )

        // Observes AllocationStateHolder and fetches items for allocation
        itemsToAllocate = AllocationStateHolder.itemIdsToAllocate
            .flatMapLatest { ids ->
                if (ids.isEmpty()) flowOf(emptyList())
                else catalogueRepository.getItemsByIds(ids.toList())
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

        // Validates allocation: all selected items must have a quantity > 0
        isAllocationValid = combine(itemsToAllocate, allocationQuantities) { items, quantities ->
            items.isNotEmpty() &&
                    items.size == quantities.size &&
                    quantities.values.none { it == 0 }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

        // Keeps allocation quantities in sync with currently selected items
        itemsToAllocate
            .onEach { items ->
                val newQuantities = items.associate { item ->
                    item.itemId to (_allocationQuantities.value[item.itemId] ?: 0)
                }
                _allocationQuantities.value = newQuantities
            }
            .launchIn(viewModelScope)

        // Load active events once on initialization
        viewModelScope.launch {
            allEvents.value = eventRepository.getActiveEvents().first()
        }
    }

    // Sets the currently selected event
    fun selectEvent(event: Event) {
        selectedEvent.value = event
    }

    // Updates quantity for a specific item
    fun updateAllocationQuantity(itemId: Int, quantity: Int) {
        _allocationQuantities.value = _allocationQuantities.value.toMutableMap().apply {
            this[itemId] = quantity
        }
    }

    // Removes an item from allocation
    fun removeAllocationItem(itemId: Int) {
        AllocationStateHolder.removeItem(itemId)
    }

    // Finalizes allocation by saving to repository and clearing state
    fun performAllocation(onSuccess: () -> Unit) {
        viewModelScope.launch {
            val event = selectedEvent.value ?: return@launch
            val quantitiesToAdd = _allocationQuantities.value
            eventRepository.stackAllocateItemsToEvent(event.eventId, quantitiesToAdd)
            AllocationStateHolder.clear()
            onSuccess()
        }
    }
}
