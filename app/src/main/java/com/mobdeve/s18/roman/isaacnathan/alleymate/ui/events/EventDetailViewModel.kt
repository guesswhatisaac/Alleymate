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
}

/**
 * A mandatory factory for creating EventDetailViewModel instances.
 * This is required because the ViewModel has a constructor that takes arguments (the repository and eventId),
 * which the default ViewModel provider does not know how to handle.
 */
class EventDetailViewModelFactory(
    private val eventRepository: EventRepository,
    private val eventId: Int
) : ViewModelProvider.Factory {

    /**
     * Creates a new instance of the given `ViewModel` class.
     */
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // Check if the requested ViewModel class is the one we know how to create
        if (modelClass.isAssignableFrom(EventDetailViewModel::class.java)) {
            // Suppress the "UNCHECKED_CAST" warning because our 'if' check makes this cast safe
            @Suppress("UNCHECKED_CAST")
            return EventDetailViewModel(eventRepository, eventId) as T
        }
        // If it's a different ViewModel class, throw an exception
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}