package com.mobdeve.s18.roman.isaacnathan.alleymate.ui.catalogue

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.AllocationStateHolder
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.local.AlleyMateDatabase
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.CatalogueItem
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.repository.CatalogueRepository
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.ItemCategory
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.repository.ItemCategoryRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class CatalogueViewModel(application: Application) : AndroidViewModel(application) {

    private val catalogueRepository: CatalogueRepository
    private val itemCategoryRepository: ItemCategoryRepository
    private val allCatalogueItems: StateFlow<List<CatalogueItem>>

    private val _selectedItemCategory = MutableStateFlow("ALL")
    val selectedItemCategory: StateFlow<String> = _selectedItemCategory.asStateFlow()

    val itemCategories: StateFlow<List<String>>
    val filteredItems: StateFlow<List<CatalogueItem>>

    private val _selectedItemIds = MutableStateFlow<Set<Int>>(emptySet())
    val selectedItemIds: StateFlow<Set<Int>> = _selectedItemIds.asStateFlow()

    private val _categoryItemCount = MutableStateFlow(0)
    val categoryItemCount: StateFlow<Int> = _categoryItemCount.asStateFlow()

    val inSelectionMode: StateFlow<Boolean> = _selectedItemIds
        .map { it.isNotEmpty() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val allocationBadgeCount: StateFlow<Int> = _selectedItemIds
        .map { it.size }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    init {
        val database = AlleyMateDatabase.getDatabase(application)
        catalogueRepository = CatalogueRepository(database.catalogueDao(), database)
        itemCategoryRepository = ItemCategoryRepository(database.itemCategoryDao())

        viewModelScope.launch {
            ensureDefaultCategoryExists()
        }

        allCatalogueItems = catalogueRepository.getAllItems()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000L),
                initialValue = emptyList()
            )

        itemCategories = itemCategoryRepository.getAllItemCategories()
            .map { dbCategories ->
                listOf("ALL") + dbCategories.map { it.name }.distinct()
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
                    items.filter { it.category == selectedCategory }
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000L),
                initialValue = emptyList()
            )
    }

    private suspend fun ensureDefaultCategoryExists() {
        itemCategoryRepository.addItemCategory(ItemCategory(name = "N/A"))
    }

    fun selectItemCategory(category: String) {
        _selectedItemCategory.value = category
    }

    fun addProduct(name: String, category: String, price: Double, stock: Int, imageUri: String?) = viewModelScope.launch {
        val standardizedCategory = if (category.isBlank()) "N/A" else category.trim().uppercase()
        val newItem = CatalogueItem(name = name, category = standardizedCategory, price = price, stock = stock, imageUri = imageUri)
        catalogueRepository.addItem(newItem)

        val newCategory = ItemCategory(name = standardizedCategory)
        itemCategoryRepository.addItemCategory(newCategory)
    }

    fun addItemCategory(name: String) = viewModelScope.launch {
        if (name.isNotBlank()) {
            val standardizedCategory = name.trim().uppercase()
            val newCategory = ItemCategory(name = standardizedCategory)
            itemCategoryRepository.addItemCategory(newCategory)
        }
    }

    fun deleteCategory(categoryName: String) = viewModelScope.launch {
        itemCategoryRepository.deleteCategoryByName(categoryName)

        if (_selectedItemCategory.value == categoryName) {
            _selectedItemCategory.value = "ALL"
        }
    }

    fun updateCategoryItemCount(category: String) {
        _categoryItemCount.value = allCatalogueItems.value.count { it.category == category }
    }

    fun editProduct(updatedItem: CatalogueItem) = viewModelScope.launch {
        // Standardize category on edit as well
        val standardizedItem = updatedItem.copy(category = updatedItem.category.trim().uppercase())
        catalogueRepository.updateItem(standardizedItem)
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
            AllocationStateHolder.addItemIds(_selectedItemIds.value)
        }
        clearSelection()
    }
}