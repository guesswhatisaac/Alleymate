package com.mobdeve.s18.roman.isaacnathan.alleymate.ui.catalogue

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.AllocationStateHolder
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.local.AlleyMateDatabase
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.CatalogueItem
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.repository.CatalogueRepository
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.ItemCategory // Import the new model
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.repository.ItemCategoryRepository // Import the new repository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class CatalogueViewModel(application: Application) : AndroidViewModel(application) {

    // --- REPOSITORIES ---
    private val catalogueRepository: CatalogueRepository
    private val itemCategoryRepository: ItemCategoryRepository

    // --- RAW DATA FLOWS ---
    private val allCatalogueItems: StateFlow<List<CatalogueItem>>

    // --- STATE FOR THE UI ---
    private val _selectedItemCategory = MutableStateFlow("ALL")
    val selectedItemCategory: StateFlow<String> = _selectedItemCategory.asStateFlow()
    val itemCategories: StateFlow<List<String>>
    val filteredItems: StateFlow<List<CatalogueItem>>

    private val _selectedItemIds = MutableStateFlow<Set<Int>>(emptySet())
    val selectedItemIds: StateFlow<Set<Int>> = _selectedItemIds.asStateFlow()
    val inSelectionMode: StateFlow<Boolean> = _selectedItemIds
        .map { it.isNotEmpty() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val allocationBadgeCount: StateFlow<Int> = AllocationStateHolder.allocationCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    init {
        val database = AlleyMateDatabase.getDatabase(application)

        catalogueRepository = CatalogueRepository(database.catalogueDao(), database)
        itemCategoryRepository = ItemCategoryRepository(database.itemCategoryDao())

        allCatalogueItems = catalogueRepository.getAllItems()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000L),
                initialValue = emptyList()
            )

        itemCategories = itemCategoryRepository.getAllItemCategories()
            .map { dbCategories ->
                val uppercasedNames = dbCategories.map { it.name.uppercase() }
                val distinctNames = uppercasedNames.distinct()
                listOf("ALL") + distinctNames
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000L),
                initialValue = listOf("ALL")
            )

        filteredItems = allCatalogueItems
            .combine(_selectedItemCategory) { items, selectedCategory ->
                if (selectedCategory == "ALL") {
                    items
                } else {
                    items.filter { it.category.equals(selectedCategory, ignoreCase = true) }
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000L),
                initialValue = emptyList()
            )
    }

    // --- USER ACTIONS ---
    fun selectItemCategory(category: String) {
        _selectedItemCategory.value = category
    }

    fun addProduct(name: String, category: String, price: Double, stock: Int, imageUri: String?) = viewModelScope.launch {
        val newItem = CatalogueItem(name = name, category = category, price = price, stock = stock, imageUri = imageUri)
        catalogueRepository.addItem(newItem)

        val newCategory = ItemCategory(name = category)
        itemCategoryRepository.addItemCategory(newCategory)
    }

    fun addItemCategory(name: String) = viewModelScope.launch {
        if (name.isNotBlank()) {
            val newCategory = ItemCategory(name = name.trim())
            itemCategoryRepository.addItemCategory(newCategory)
        }
    }

    fun editProduct(updatedItem: CatalogueItem) = viewModelScope.launch {
        catalogueRepository.updateItem(updatedItem)
    }

    fun restockProduct(item: CatalogueItem, quantityToAdd: Int) = viewModelScope.launch {
        catalogueRepository.restockItem(item, quantityToAdd)
    }

    fun deleteItem(item: CatalogueItem) = viewModelScope.launch {
        catalogueRepository.deleteItem(item)
    }

    fun toggleSelection(itemId: Int) {
        val currentSelection = _selectedItemIds.value.toMutableSet()
        if (itemId in currentSelection) {
            currentSelection.remove(itemId)
        } else {
            currentSelection.add(itemId)
        }
        _selectedItemIds.value = currentSelection
    }


    fun clearSelection() {
        _selectedItemIds.value = emptySet()
    }

    fun prepareForAllocation() {

        val newlySelectedItems = allCatalogueItems.value
            .filter { it.itemId in _selectedItemIds.value }

        if (newlySelectedItems.isNotEmpty()) {
            AllocationStateHolder.addItems(newlySelectedItems)
        }

        clearSelection()

    }

}