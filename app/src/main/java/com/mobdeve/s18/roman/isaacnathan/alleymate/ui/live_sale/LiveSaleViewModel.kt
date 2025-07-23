package com.mobdeve.s18.roman.isaacnathan.alleymate.ui.live_sale

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.local.AlleyMateDatabase
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.*
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.repository.EventRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

// --- UI State Classes ---
// Transaction interaction phases
sealed interface AddTransactionState {
    data object Hidden : AddTransactionState
    data object Selecting : AddTransactionState
    data object Transacting : AddTransactionState
}

// Modal types for the live sale screen
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
    private val eventFlow = eventRepository.getEventById(eventId)
    private val inventoryFlow = eventRepository.getInventoryForEvent(eventId)
    private val expensesFlow = eventRepository.getExpensesForEvent(eventId)
    private val transactionsFlow = eventRepository.getTransactionsForEvent(eventId)

    // --- UI State Flow ---
    val uiState: StateFlow<LiveSaleUiState> = combine(
        eventFlow,
        inventoryFlow,
        expensesFlow,
        transactionsFlow
    ) { event, inventory, expenses, transactions ->

        // Aggregate stats
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

    // --- Modal States ---
    private val _modalState = MutableStateFlow<LiveSaleModalState>(LiveSaleModalState.Hidden)
    val modalState = _modalState.asStateFlow()

    private val _addTransactionState = MutableStateFlow<AddTransactionState>(AddTransactionState.Hidden)
    val addTransactionState = _addTransactionState.asStateFlow()

    // --- Transaction Cart State ---
    private val _selectedItemsForTransaction = MutableStateFlow<Set<Int>>(emptySet())
    val selectedItemsForTransaction = _selectedItemsForTransaction.asStateFlow()

    private val _transactionCart = MutableStateFlow<Map<Int, Int>>(emptyMap())
    val transactionCart = _transactionCart.asStateFlow()

    // --- UI Actions ---

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

    // Selects/deselects an item for transaction
    fun toggleItemSelection(itemId: Int) {
        val currentSelection = _selectedItemsForTransaction.value.toMutableSet()
        if (!currentSelection.add(itemId)) currentSelection.remove(itemId)
        _selectedItemsForTransaction.value = currentSelection
    }

    // Moves from selecting items → quantity input
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

    // Adds a new expense to the event
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

    // Finalizes and records a new transaction
    fun recordSale() = viewModelScope.launch {
        val cartQuantities = _transactionCart.value.filter { it.value > 0 }
        if (cartQuantities.isEmpty()) {
            dismissAddTransaction()
            return@launch
        }

        // Match selected items with their catalogue details
        val currentInventory = uiState.value.inventory
        val cartItemsWithDetails = currentInventory
            .filter { it.catalogueItem.itemId in cartQuantities.keys }
            .associateBy { it.catalogueItem.itemId }
            .mapValues { it.value.catalogueItem }

        eventRepository.recordSaleTransaction(eventId, cartItemsWithDetails, cartQuantities)

        dismissAddTransaction()
    }

    // Ends the event and returns unsold stock
    fun endLiveSale() = viewModelScope.launch {
        uiState.value.event?.let { currentEvent ->
            eventRepository.endSaleAndReturnStock(currentEvent.eventId)
        }
    }
}

// Factory to construct LiveSaleViewModel with event ID and database dependency
class LiveSaleViewModelFactory(
    private val application: Application,
    private val eventId: Int
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LiveSaleViewModel::class.java)) {
            val db = AlleyMateDatabase.getDatabase(application)
            val repository = EventRepository(
                db.eventDao(),
                db.catalogueDao(),
                db.transactionDao()
            )
            @Suppress("UNCHECKED_CAST")
            return LiveSaleViewModel(repository, eventId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
