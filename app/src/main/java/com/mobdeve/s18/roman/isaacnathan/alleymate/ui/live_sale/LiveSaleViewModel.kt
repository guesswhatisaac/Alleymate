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

// --- UI State Classes ---
sealed interface AddTransactionState {
    data object Hidden : AddTransactionState
    data object Selecting : AddTransactionState
    data object Transacting : AddTransactionState
}

sealed interface LiveSaleModalState {
    data object Hidden : LiveSaleModalState
    data object AddTransaction : LiveSaleModalState
    data object AddExpense : LiveSaleModalState
}

class LiveSaleViewModel(
    private val eventRepository: EventRepository,
    private val eventId: Int
) : ViewModel() {

    // --- Raw Data Flows ---
    private val eventFlow: Flow<Event?> = eventRepository.getEventById(eventId)
    private val inventoryFlow: Flow<List<EventInventoryWithDetails>> = eventRepository.getInventoryForEvent(eventId)
    private val expensesFlow: Flow<List<EventExpense>> = eventRepository.getExpensesForEvent(eventId)
    private val transactionsFlow: Flow<List<TransactionWithItems>> = eventRepository.getTransactionsForEvent(eventId)

    // --- UI State Flow ---
    val uiState: StateFlow<LiveSaleUiState> = combine(
        eventFlow,
        inventoryFlow,
        expensesFlow,
        transactionsFlow
    ) { event, inventory, expenses, transactions ->
        val totalItemsAllocated = inventory.sumOf { it.eventInventoryItem.allocatedQuantity }
        val totalItemsSold = transactions.sumOf { t -> t.items.sumOf { i -> i.saleTransactionItem.quantity } }
        val totalRevenue = transactions.sumOf { t -> t.items.sumOf { i -> i.saleTransactionItem.quantity * i.saleTransactionItem.priceInCents } }
        val totalExpenses = expenses.sumOf { it.amountInCents }

        LiveSaleUiState(
            event = event,
            inventory = inventory,
            expenses = expenses,
            transactions = transactions,
            totalItemsSold = totalItemsSold,
            totalRevenueInCents = totalRevenue,
            totalExpensesInCents = totalExpenses,
            profitInCents = totalRevenue - totalExpenses,
            totalStockLeft = totalItemsAllocated - totalItemsSold
        )
    }.stateIn(viewModelScope, SharingStarted.Eagerly, LiveSaleUiState())

    // --- Modal State ---
    private val _modalState = MutableStateFlow<LiveSaleModalState>(LiveSaleModalState.Hidden)
    val modalState: StateFlow<LiveSaleModalState> = _modalState.asStateFlow()

    private val _addTransactionState = MutableStateFlow<AddTransactionState>(AddTransactionState.Hidden)
    val addTransactionState: StateFlow<AddTransactionState> = _addTransactionState.asStateFlow()

    // --- Transaction Cart State ---
    private val _selectedItemsForTransaction = MutableStateFlow<Set<Int>>(emptySet())
    val selectedItemsForTransaction: StateFlow<Set<Int>> = _selectedItemsForTransaction.asStateFlow()

    private val _transactionCart = MutableStateFlow<Map<Int, Int>>(emptyMap())
    val transactionCart: StateFlow<Map<Int, Int>> = _transactionCart.asStateFlow()

    //private val _currentCart = MutableStateFlow<Map<Int, Int>>(emptyMap())
    //val currentCart: StateFlow<Map<Int, Int>> = _currentCart.asStateFlow()

    // --- UI Actions ---
    /*fun beginAddTransaction() {
        _addTransactionState.value = AddTransactionState.Selecting
    }*/

    fun dismissAddTransaction() {
        _addTransactionState.value = AddTransactionState.Hidden
        _selectedItemsForTransaction.value = emptySet()
        _transactionCart.value = emptyMap()
    }

    fun showAddTransactionModal() {
        _modalState.value = LiveSaleModalState.AddTransaction
        _addTransactionState.value = AddTransactionState.Selecting
    }

    fun showAddExpenseModal() {
        _modalState.value = LiveSaleModalState.AddExpense
    }

    fun dismissAllModals() {
        _modalState.value = LiveSaleModalState.Hidden
        _addTransactionState.value = AddTransactionState.Hidden
        _selectedItemsForTransaction.value = emptySet()
        _transactionCart.value = emptyMap()
    }

    fun toggleItemSelection(itemId: Int) {
        val currentSelection = _selectedItemsForTransaction.value.toMutableSet()
        if (currentSelection.contains(itemId)) {
            currentSelection.remove(itemId)
        } else {
            currentSelection.add(itemId)
        }
        _selectedItemsForTransaction.value = currentSelection
    }

    fun proceedToTransact() {
        if (_selectedItemsForTransaction.value.isEmpty()) return
        _transactionCart.value = _selectedItemsForTransaction.value.associateWith { 1 }
        _addTransactionState.value = AddTransactionState.Transacting
    }

    fun updateTransactionQuantity(itemId: Int, quantity: Int) {
        _transactionCart.value = _transactionCart.value.toMutableMap().apply {
            this[itemId] = quantity
        }
    }

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

    fun recordSale() = viewModelScope.launch {
        val cartQuantities = _transactionCart.value.filter { it.value > 0 }
        if (cartQuantities.isEmpty()) {
            dismissAddTransaction()
            return@launch
        }

        val currentInventory = uiState.value.inventory
        val cartItemsWithDetails = currentInventory
            .filter { it.catalogueItem.itemId in cartQuantities.keys }
            .associateBy { it.catalogueItem.itemId }
            .mapValues { it.value.catalogueItem }

        eventRepository.recordSaleTransaction(eventId, cartItemsWithDetails, cartQuantities)

        dismissAddTransaction()
    }

    fun endLiveSale() = viewModelScope.launch {
        uiState.value.event?.let { currentEvent ->
            eventRepository.endSaleAndReturnStock(currentEvent.eventId)
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
