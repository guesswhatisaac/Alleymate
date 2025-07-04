package com.mobdeve.s18.roman.isaacnathan.alleymate.data.local

import android.content.Context
import androidx.room.*
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.CatalogueItem
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.ItemCategory
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.local.ItemCategoryDao

@Database(
    entities = [CatalogueItem::class, ItemCategory::class],
    version = 3,
    exportSchema = false
)

abstract class AlleyMateDatabase : RoomDatabase() {

    abstract fun catalogueDao(): CatalogueDao
    abstract fun itemCategoryDao(): ItemCategoryDao

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