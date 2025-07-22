package com.mobdeve.s18.roman.isaacnathan.alleymate.data.repository

import com.mobdeve.s18.roman.isaacnathan.alleymate.data.local.ItemCategoryDao
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.ItemCategory
import kotlinx.coroutines.flow.Flow

class ItemCategoryRepository(private val itemCategoryDao: ItemCategoryDao) {

    fun getAllItemCategories(): Flow<List<ItemCategory>> = itemCategoryDao.getAllItemCategories()

    suspend fun addItemCategory(itemCategory: ItemCategory) {
        itemCategoryDao.insert(itemCategory)
    }

    suspend fun deleteCategoryByName(categoryName: String) {
        itemCategoryDao.deleteCategoryByName(categoryName)
    }
}