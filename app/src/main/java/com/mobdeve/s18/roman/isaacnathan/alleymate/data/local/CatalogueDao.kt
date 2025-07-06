package com.mobdeve.s18.roman.isaacnathan.alleymate.data.local

import androidx.room.*
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.CatalogueItem
import kotlinx.coroutines.flow.Flow

@Dao
interface CatalogueDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: CatalogueItem)

    @Update
    suspend fun update(item: CatalogueItem)

    @Delete
    suspend fun delete(item: CatalogueItem)

    @Query("SELECT * FROM catalogue_items ORDER BY name ASC")
    fun getAllItems(): Flow<List<CatalogueItem>>

    @Query("DELETE FROM catalogue_items")
    suspend fun deleteAllItems()

    @Query("UPDATE catalogue_items SET stock = stock - :quantityToReduce WHERE itemId = :itemId")
    suspend fun reduceStock(itemId: Int, quantityToReduce: Int)

    @Query("SELECT * FROM catalogue_items WHERE itemId IN (:itemIds)")
    fun getItemsByIds(itemIds: List<Int>): Flow<List<CatalogueItem>>


}