package com.mobdeve.s18.roman.isaacnathan.alleymate.data.local

import android.content.Context
import androidx.room.*
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.CatalogueItem
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.ItemCategory
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.Event
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.EventExpense
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.EventInventoryItem

// vv  TODO: A CERTAIN FUNCTION MUST BE REMOVED IN PRODUCTION; CREATE MIGRATION SYSTEM
@Database(
    entities = [
        CatalogueItem::class,
        ItemCategory::class,
        Event::class,
        EventExpense::class,
        EventInventoryItem::class
    ],
    version = 5,
    exportSchema = false
)

abstract class AlleyMateDatabase : RoomDatabase() {

    abstract fun catalogueDao(): CatalogueDao
    abstract fun itemCategoryDao(): ItemCategoryDao
    abstract fun eventDao(): EventDao

    fun clearDatabase() { // DEBUG PURPOSES
        this.clearAllTables()
    }

    companion object {
        @Volatile
        private var INSTANCE: AlleyMateDatabase? = null

        fun getDatabase(context: Context): AlleyMateDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AlleyMateDatabase::class.java,
                    "alleymate_database"
                )
                    .fallbackToDestructiveMigration(true) // TODO: MUST BE REMOVED IN PRODUCTION; CREATE MIGRATION SYSTEM
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}