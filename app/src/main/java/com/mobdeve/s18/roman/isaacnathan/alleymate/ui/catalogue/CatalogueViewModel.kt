package com.mobdeve.s18.roman.isaacnathan.alleymate.ui.catalogue

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
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
    private val allCatalogueItems: Flow<List<CatalogueItem>>

    // --- STATE FOR THE UI ---
    private val _selectedItemCategory = MutableStateFlow("ALL")
    val selectedItemCategory: StateFlow<String> = _selectedItemCategory.asStateFlow()
    val itemCategories: StateFlow<List<String>>
    val filteredItems: StateFlow<List<CatalogueItem>>


    init {
        val database = AlleyMateDatabase.getDatabase(application)

        catalogueRepository = CatalogueRepository(database.catalogueDao(), database)
        itemCategoryRepository = ItemCategoryRepository(database.itemCategoryDao())

        allCatalogueItems = catalogueRepository.getAllItems()

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
}