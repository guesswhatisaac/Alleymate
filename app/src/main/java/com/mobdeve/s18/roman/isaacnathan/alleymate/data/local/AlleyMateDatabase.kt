package com.mobdeve.s18.roman.isaacnathan.alleymate.data.local

import android.content.Context
import androidx.room.*
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.*
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.views.EventSummaryView

// TODO: Remove fallbackToDestructiveMigration in production and implement a migration system
@TypeConverters(EventStatusConverter::class)
@Database(
    entities = [
        CatalogueItem::class,
        ItemCategory::class,
        Event::class,
        EventExpense::class,
        EventInventoryItem::class,
        SaleTransaction::class,
        SaleTransactionItem::class
    ],
    views = [EventSummaryView::class],
    version = 19,
    exportSchema = false
)
abstract class AlleyMateDatabase : RoomDatabase() {

    abstract fun catalogueDao(): CatalogueDao
    abstract fun itemCategoryDao(): ItemCategoryDao
    abstract fun eventDao(): EventDao
    abstract fun transactionDao(): TransactionDao

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
                    .fallbackToDestructiveMigration(true) // TODO: Remove in production
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}
