package com.mobdeve.s18.roman.isaacnathan.alleymate.ui.live_sale

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.local.AlleyMateDatabase
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.*
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.relations.*
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.relations.TransactionWithItems
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.repository.EventRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

sealed interface AddTransactionState {
    data object Hidden : AddTransactionState
    data object Selecting : AddTransactionState
    data object Transacting : AddTransactionState
}

class LiveSaleViewModel(
    private val eventRepository: EventRepository,
    private val eventId: Int
) : ViewModel() {

    private val eventFlow: Flow<Event?> = eventRepository.getEventById(eventId)
    private val inventoryFlow: Flow<List<EventInventoryWithDetails>> = eventRepository.getInventoryForEvent(eventId)
    private val expensesFlow: Flow<List<EventExpense>> = eventRepository.getExpensesForEvent(eventId)
    val transactionsFlow: Flow<List<TransactionWithItems>> = eventRepository.getTransactionsForEvent(eventId)

    val event: StateFlow<Event?> = combine(
        eventFlow,
        inventoryFlow,
        expensesFlow,
        transactionsFlow // We must also listen to transaction changes
    ) { event, inventory, expenses, transactions ->
        // This block will now re-run whenever a new transaction is added.
        event?.apply {
            catalogueCount = inventory.size
            totalItemsAllocated = inventory.sumOf { it.eventInventoryItem.allocatedQuantity }

            // Recalculate sold items based on persisted transactions
            val soldItemsMap = mutableMapOf<Int, Int>()
            transactions.forEach { trans ->
                trans.items.forEach { item ->
                    soldItemsMap[item.saleTransactionItem.itemId] = (soldItemsMap[item.saleTransactionItem.itemId] ?: 0) + item.saleTransactionItem.quantity
                }
            }
            totalItemsSold = soldItemsMap.values.sum()

            totalStockLeft = totalItemsAllocated - totalItemsSold
            totalExpensesInCents = expenses.sumOf { it.amountInCents }

            totalRevenueInCents = transactions.flatMap { it.items }.sumOf {
                it.saleTransactionItem.quantity * it.saleTransactionItem.priceInCents
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)



    val inventory: StateFlow<List<EventInventoryWithDetails>> = eventRepository.getInventoryForEvent(eventId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val expenses: StateFlow<List<EventExpense>> = eventRepository.getExpensesForEvent(eventId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val transactions: StateFlow<List<TransactionWithItems>> = eventRepository.getTransactionsForEvent(eventId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())


    private val _addTransactionState = MutableStateFlow<AddTransactionState>(AddTransactionState.Hidden)
    val addTransactionState: StateFlow<AddTransactionState> = _addTransactionState.asStateFlow()

    private val _selectedItemsForTransaction = MutableStateFlow<Set<Int>>(emptySet())
    val selectedItemsForTransaction: StateFlow<Set<Int>> = _selectedItemsForTransaction.asStateFlow()

    private val _transactionCart = MutableStateFlow<Map<Int, Int>>(emptyMap())
    val transactionCart: StateFlow<Map<Int, Int>> = _transactionCart.asStateFlow()

    private val _currentCart = MutableStateFlow<Map<Int, Int>>(emptyMap())
    val currentCart: StateFlow<Map<Int, Int>> = _currentCart.asStateFlow()

    fun beginAddTransaction() { _addTransactionState.value = AddTransactionState.Selecting }
    fun dismissAddTransaction() {
        _addTransactionState.value = AddTransactionState.Hidden
        _selectedItemsForTransaction.value = emptySet()
        _transactionCart.value = emptyMap()
    }

    fun toggleItemSelection(itemId: Int) {
        val currentSelection = _selectedItemsForTransaction.value.toMutableSet()
        if (currentSelection.contains(itemId)) currentSelection.remove(itemId) else currentSelection.add(itemId)
        _selectedItemsForTransaction.value = currentSelection
    }

    fun proceedToTransact() {
        if (_selectedItemsForTransaction.value.isEmpty()) return
        _transactionCart.value = _selectedItemsForTransaction.value.associateWith { 1 } // Start with quantity 1
        _addTransactionState.value = AddTransactionState.Transacting
    }

    fun updateTransactionQuantity(itemId: Int, quantity: Int) {
        _transactionCart.value = _transactionCart.value.toMutableMap().apply { this[itemId] = quantity }
    }

    fun recordSale() = viewModelScope.launch {
        val cartQuantities = _transactionCart.value.filter { it.value > 0 }
        if (cartQuantities.isEmpty()) {
            dismissAddTransaction()
            return@launch
        }

        // We need the full CatalogueItem details to get the price at time of sale
        val cartItemsWithDetails = inventory.value
            .filter { it.catalogueItem.itemId in cartQuantities.keys }
            .associateBy { it.catalogueItem.itemId }
            .mapValues { it.value.catalogueItem }

        eventRepository.recordSaleTransaction(eventId, cartItemsWithDetails, cartQuantities)
        dismissAddTransaction()
    }

    fun endLiveSale() = viewModelScope.launch {
        event.value?.let { currentEvent ->
            val updatedEvent = currentEvent.copy(status = EventStatus.ENDED)
            eventRepository.updateEvent(updatedEvent)
        }
    }
}

// --- ViewModel Factory ---
class LiveSaleViewModelFactory(
    private val application: Application,
    private val eventId: Int
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LiveSaleViewModel::class.java)) {
            val db = AlleyMateDatabase.getDatabase(application)

            val repository = EventRepository(db.eventDao(), db.catalogueDao(), db.transactionDao())
            @Suppress("UNCHECKED_CAST")
            return LiveSaleViewModel(repository, eventId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}