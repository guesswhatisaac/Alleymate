package com.mobdeve.s18.roman.isaacnathan.alleymate.ui.live_sale

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.local.AlleyMateDatabase
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.*
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.relations.EventInventoryWithDetails
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.repository.EventRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class LiveSaleViewModel(
    private val eventRepository: EventRepository,
    val eventId: Int
) : ViewModel() {

    // --- State for UI ---
    val event: StateFlow<Event?> = eventRepository.getEventById(eventId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val inventory: StateFlow<List<EventInventoryWithDetails>> = eventRepository.getInventoryForEvent(eventId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // In-memory list of transactions for this session
    private val _transactions = MutableStateFlow<List<Transaction>>(emptyList())
    val transactions: StateFlow<List<Transaction>> = _transactions.asStateFlow()

    // Key: itemId, Value: quantity
    private val _currentCart = MutableStateFlow<Map<Int, Int>>(emptyMap())
    val currentCart: StateFlow<Map<Int, Int>> = _currentCart.asStateFlow()

    // --- User Actions ---
    fun addItemToCart(itemId: Int, quantity: Int) {
        val newCart = _currentCart.value.toMutableMap()
        if (quantity > 0) {
            newCart[itemId] = quantity
        } else {
            newCart.remove(itemId)
        }
        _currentCart.value = newCart
    }

    fun recordSale() {
        viewModelScope.launch {
            val cart = _currentCart.value
            if (cart.isEmpty()) return@launch

            // TODO: This is where you would call the repository to persist the sale
            // For now, we will just create a transaction object for the UI

            val timeFormatter = SimpleDateFormat("hh:mm a", Locale.getDefault())
            val timestamp = timeFormatter.format(Date())

            val transactionItems = cart.mapNotNull { (itemId, quantity) ->
                val inventoryDetails = inventory.value.find { it.catalogueItem.itemId == itemId }
                inventoryDetails?.let {
                    TransactionItem(
                        id = it.catalogueItem.itemId,
                        name = it.catalogueItem.name,
                        category = it.catalogueItem.category,
                        price = it.catalogueItem.price.toInt(), // Assuming price is Int for TransactionItem
                        quantity = quantity
                    )
                }
            }

            val newTransaction = Transaction(
                id = (_transactions.value.size + 1),
                time = timestamp,
                items = transactionItems
            )

            // Add to our in-memory list and clear the cart
            _transactions.value = listOf(newTransaction) + _transactions.value
            _currentCart.value = emptyMap()
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
            val repository = EventRepository(db.eventDao(), db.catalogueDao())
            @Suppress("UNCHECKED_CAST")
            return LiveSaleViewModel(repository, eventId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}