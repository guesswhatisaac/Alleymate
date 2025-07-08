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

class LiveSaleViewModel(
    private val eventRepository: EventRepository,
    private val eventId: Int
) : ViewModel() {

    val event: StateFlow<Event?> = eventRepository.getEventById(eventId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val inventory: StateFlow<List<EventInventoryWithDetails>> = eventRepository.getInventoryForEvent(eventId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val expenses: StateFlow<List<EventExpense>> = eventRepository.getExpensesForEvent(eventId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val transactions: StateFlow<List<TransactionWithItems>> = eventRepository.getTransactionsForEvent(eventId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _currentCart = MutableStateFlow<Map<Int, Int>>(emptyMap())
    val currentCart: StateFlow<Map<Int, Int>> = _currentCart.asStateFlow()


    // --- User Actions ---
    fun addItemToCart(itemId: Int) {
        val newCart = _currentCart.value.toMutableMap()
        // Increment quantity by 1
        newCart[itemId] = (newCart[itemId] ?: 0) + 1
        _currentCart.value = newCart
    }


    fun clearCart() {
        _currentCart.value = emptyMap()
    }


    fun recordSale() {
        viewModelScope.launch {
            val cartQuantities = _currentCart.value
            if (cartQuantities.isEmpty()) return@launch

            val cartItemsWithDetails = inventory.value
                .filter { it.catalogueItem.itemId in cartQuantities.keys }
                .associateBy { it.catalogueItem.itemId }
                .mapValues { it.value.catalogueItem }

            eventRepository.recordSaleTransaction(eventId, cartItemsWithDetails, cartQuantities)

            clearCart()
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