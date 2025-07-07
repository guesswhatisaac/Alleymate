package com.mobdeve.s18.roman.isaacnathan.alleymate.ui.events

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.Event
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.EventExpense
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.relations.EventInventoryWithDetails
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.repository.EventRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class EventDetailViewModel(
    private val eventRepository: EventRepository,
    private val eventId: Int
) : ViewModel() {

    private val eventFlow: Flow<Event?> = eventRepository.getEventById(eventId)
    private val inventoryFlow: Flow<List<EventInventoryWithDetails>> = eventRepository.getInventoryForEvent(eventId)
    val expensesFlow: Flow<List<EventExpense>> = eventRepository.getExpensesForEvent(eventId)


    val event: StateFlow<Event?> = combine(
        eventFlow,
        inventoryFlow,
        expensesFlow
    ) { event, inventory, expenses ->
        event?.apply {
            totalItemsAllocated = inventory.sumOf { it.eventInventoryItem.allocatedQuantity }
            totalItemsSold = inventory.sumOf { it.eventInventoryItem.soldQuantity }
            totalRevenueInCents = inventory.sumOf {
                it.eventInventoryItem.soldQuantity * (it.catalogueItem.price * 100).toLong()
            }
            totalExpensesInCents = expenses.sumOf { it.amountInCents }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    val inventory: StateFlow<List<EventInventoryWithDetails>> = inventoryFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val expenses: StateFlow<List<EventExpense>> = expensesFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addExpense(description: String, amount: Double) = viewModelScope.launch {
        // Basic validation to ensure data is sensible
        if (description.isNotBlank() && amount > 0) {
            val newExpense = EventExpense(
                eventId = eventId,
                description = description,
                amountInCents = (amount * 100).toLong()
            )
            eventRepository.insertExpense(newExpense)
        }
    }

    fun allocateMoreItems(itemsToAllocate: Map<Int, Int>) {
        viewModelScope.launch {
            eventRepository.stackAllocateItemsToEvent(eventId, itemsToAllocate)

            itemsToAllocate.forEach { (itemId, quantity) ->
                eventRepository.reduceCatalogueStock(itemId, quantity)
            }
        }
    }

    fun updateEvent(event: Event) = viewModelScope.launch {
        eventRepository.updateEvent(event)
    }

    fun deleteEvent() = viewModelScope.launch {
        event.value?.let { currentEvent ->
            eventRepository.deleteEvent(currentEvent)
        }
    }




}


class EventDetailViewModelFactory(
    private val eventRepository: EventRepository,
    private val eventId: Int
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EventDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return EventDetailViewModel(eventRepository, eventId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}