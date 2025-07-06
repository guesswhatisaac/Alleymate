package com.mobdeve.s18.roman.isaacnathan.alleymate.data.repository

import com.mobdeve.s18.roman.isaacnathan.alleymate.data.local.CatalogueDao
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.CatalogueItem
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.local.AlleyMateDatabase
import kotlinx.coroutines.flow.Flow

class CatalogueRepository(
    private val catalogueDao: CatalogueDao,
    private val database: AlleyMateDatabase
) {

    fun getAllItems(): Flow<List<CatalogueItem>> = catalogueDao.getAllItems()

    suspend fun addItem(item: CatalogueItem) {
        catalogueDao.insert(item)
    }

    suspend fun updateItem(item: CatalogueItem) {
        catalogueDao.update(item)
    }

    suspend fun restockItem(item: CatalogueItem, quantityToAdd: Int) {
        val updatedItem = item.copy(stock = item.stock + quantityToAdd)
        catalogueDao.update(updatedItem)
    }

    suspend fun deleteItem(item: CatalogueItem) {
        catalogueDao.delete(item)
    }

    suspend fun deleteAllItems() {
        catalogueDao.deleteAllItems()
    }

    fun clearEntireDatabase() {
        database.clearAllData()
    }

    fun getItemsByIds(itemIds: List<Int>): Flow<List<CatalogueItem>> {
        return catalogueDao.getItemsByIds(itemIds)
    }

}