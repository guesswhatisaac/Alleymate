package com.mobdeve.s18.roman.isaacnathan.alleymate.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.ItemCategory
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemCategoryDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(itemCategory: ItemCategory)

    @Query("SELECT * FROM item_categories ORDER BY name ASC")
    fun getAllItemCategories(): Flow<List<ItemCategory>>
}