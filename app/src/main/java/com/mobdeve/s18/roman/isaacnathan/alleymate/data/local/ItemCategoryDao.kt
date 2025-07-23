package com.mobdeve.s18.roman.isaacnathan.alleymate.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.ItemCategory
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemCategoryDao {

    // Insert new item category; ignores if conflict
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(itemCategory: ItemCategory)

    // Get all item categories ordered by name
    @Query("SELECT * FROM item_categories ORDER BY name ASC")
    fun getAllItemCategories(): Flow<List<ItemCategory>>

    // Delete category by its name
    @Query("DELETE FROM item_categories WHERE name = :categoryName")
    suspend fun deleteCategoryByName(categoryName: String)
}
