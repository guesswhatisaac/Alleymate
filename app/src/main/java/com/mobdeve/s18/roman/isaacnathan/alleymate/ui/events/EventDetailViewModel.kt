package com.mobdeve.s18.roman.isaacnathan.alleymate.ui.events

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.Event
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.EventExpense
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.EventStatus
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.relations.EventInventoryWithDetails
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.repository.EventRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class EventDetailViewModel(
    private val eventRepository: EventRepository,
    private val eventId: Int
) : ViewModel() {

    // --- Flows from Repository ---
    private val eventFlow: Flow<Event?> = eventRepository.getEventById(eventId)
    private val inventoryFlow: Flow<List<EventInventoryWithDetails>> = eventRepository.getInventoryForEvent(eventId)
    val expensesFlow: Flow<List<EventExpense>> = eventRepository.getExpensesForEvent(eventId)

    // --- Combined Event Flow with Calculated Fields ---
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

    // --- StateFlows for Inventory and Expenses ---
    val inventory: StateFlow<List<EventInventoryWithDetails>> = inventoryFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val expenses: StateFlow<List<EventExpense>> = expensesFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // --- Add Expense ---
    fun addExpense(description: String, amount: Double) = viewModelScope.launch {
        if (description.isNotBlank() && amount > 0) {
            val newExpense = EventExpense(
                eventId = eventId,
                description = description,
                amountInCents = (amount * 100).toLong()
            )
            eventRepository.insertExpense(newExpense)
        }
    }

    // --- Update Event ---
    fun updateEvent(event: Event) = viewModelScope.launch {
        eventRepository.updateEvent(event)
    }

    // --- Delete Event ---
    fun deleteEvent() = viewModelScope.launch {
        event.value?.let { currentEvent ->
            eventRepository.deleteEvent(currentEvent)
        }
    }


    // --- Error States for Live Sale Start ---
    private val _startSaleError = MutableStateFlow<String?>(null)
    val startSaleError: StateFlow<String?> = _startSaleError.asStateFlow()

    fun onStartSaleErrorShown() {
        _startSaleError.value = null
    }

    private val _startSaleConflictError = MutableStateFlow<String?>(null)
    val startSaleConflictError: StateFlow<String?> = _startSaleConflictError.asStateFlow()

    fun onConflictDialogDismissed() {
        _startSaleConflictError.value = null
    }

    // --- Start Live Sale with Conflict Check ---
    fun startLiveSale(onSuccess: () -> Unit) = viewModelScope.launch {
        val otherLiveEvents = eventRepository.getLiveEvents().first()
        if (otherLiveEvents.any { it.eventId != eventId }) {
            _startSaleConflictError.value = otherLiveEvents.first().title
            return@launch
        }

        event.value?.let { currentEvent ->
            val updatedEvent = currentEvent.copy(status = EventStatus.LIVE)
            eventRepository.updateEvent(updatedEvent)
            onSuccess()
        }
    }
}

// --- ViewModel Factory ---
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
