package com.mobdeve.s18.roman.isaacnathan.alleymate.data.repository

import com.mobdeve.s18.roman.isaacnathan.alleymate.data.local.ItemCategoryDao
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.ItemCategory
import kotlinx.coroutines.flow.Flow

/**
 * Repository for accessing and managing item categories.
 * Provides a clean API for data operations using [ItemCategoryDao].
 */
class ItemCategoryRepository(
    private val itemCategoryDao: ItemCategoryDao
) {

    /**
     * Retrieves all item categories from the database.
     */
    fun getAllItemCategories(): Flow<List<ItemCategory>> = itemCategoryDao.getAllItemCategories()

    /**
     * Inserts a new item category into the database.
     */
    suspend fun addItemCategory(itemCategory: ItemCategory) {
        itemCategoryDao.insert(itemCategory)
    }

    /**
     * Deletes an item category by its name.
     */
    suspend fun deleteCategoryByName(categoryName: String) {
        itemCategoryDao.deleteCategoryByName(categoryName)
    }
}
