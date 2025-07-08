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
    val itemsToAllocate: StateFlow<List<CatalogueItem>>
    private val _allocationQuantities = MutableStateFlow<Map<Int, Int>>(emptyMap())
    val allocationQuantities = _allocationQuantities.asStateFlow()
    val isAllocationValid: StateFlow<Boolean>

    init {
        val database = AlleyMateDatabase.getDatabase(application)
        catalogueRepository = CatalogueRepository(database.catalogueDao(), database)
        eventRepository = EventRepository(database.eventDao(), database.catalogueDao(), database.transactionDao())

        itemsToAllocate = AllocationStateHolder.itemIdsToAllocate
            .flatMapLatest { ids ->
                if (ids.isEmpty()) {
                    flowOf(emptyList())
                } else {
                    catalogueRepository.getItemsByIds(ids.toList())
                }
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

        isAllocationValid = combine(
            itemsToAllocate,
            allocationQuantities
        ) { items, quantities ->
            items.isNotEmpty() && items.size == quantities.size && quantities.values.none { it == 0 }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )
        itemsToAllocate
            .onEach { items ->
                val newQuantities = items.associate { item ->
                    item.itemId to (_allocationQuantities.value[item.itemId] ?: 0)
                }
                _allocationQuantities.value = newQuantities
            }
            .launchIn(viewModelScope)

        viewModelScope.launch {
            allEvents.value = eventRepository.getAllEvents().first()
        }
    }

    // --- PUBLIC USER ACTIONS  ---

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
            val quantitiesToAdd = _allocationQuantities.value
            eventRepository.stackAllocateItemsToEvent(event.eventId, quantitiesToAdd)
            AllocationStateHolder.clear()
            onSuccess()
        }
    }
}