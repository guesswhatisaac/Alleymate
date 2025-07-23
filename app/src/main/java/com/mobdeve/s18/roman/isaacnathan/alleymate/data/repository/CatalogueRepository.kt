package com.mobdeve.s18.roman.isaacnathan.alleymate.data.repository

import com.mobdeve.s18.roman.isaacnathan.alleymate.data.local.CatalogueDao
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.CatalogueItem
import kotlinx.coroutines.flow.Flow

/**
 * Repository that abstracts access to catalogue data operations.
 * Delegates data access to the [CatalogueDao] and supports
 * both read and write operations on catalogue items.
 */
class CatalogueRepository(
    private val catalogueDao: CatalogueDao,
) {

    /** Returns a stream of all catalogue items in the database. */
    fun getAllItems(): Flow<List<CatalogueItem>> =
        catalogueDao.getAllItems()

    /** Adds a new catalogue item to the database. */
    suspend fun addItem(item: CatalogueItem) {
        catalogueDao.insert(item)
    }

    /** Updates an existing catalogue item. */
    suspend fun updateItem(item: CatalogueItem) {
        catalogueDao.update(item)
    }

    /**
     * Increases the stock of a catalogue item by the specified amount.
     * Creates a copy with updated stock and saves it.
     */
    suspend fun restockItem(item: CatalogueItem, quantityToAdd: Int) {
        val updatedItem = item.copy(stock = item.stock + quantityToAdd)
        catalogueDao.update(updatedItem)
    }

    /** Removes a catalogue item from the database. */
    suspend fun deleteItem(item: CatalogueItem) {
        catalogueDao.delete(item)
    }

    /**
     * Returns a stream of catalogue items matching the provided list of IDs.
     */
    fun getItemsByIds(itemIds: List<Int>): Flow<List<CatalogueItem>> =
        catalogueDao.getItemsByIds(itemIds)

    /**
     * Returns a stream representing the count of items whose stock is below a given threshold.
     */
    fun getLowStockItemsCount(threshold: Int): Flow<Int> =
        catalogueDao.getLowStockItemsCount(threshold)
}
