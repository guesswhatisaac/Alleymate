package com.mobdeve.s18.roman.isaacnathan.alleymate.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

// Represents a catalogue item that can be allocated and sold in events.
@Entity(tableName = "catalogue_items")
data class CatalogueItem(

    // Identification
    @PrimaryKey(autoGenerate = true)
    val itemId: Int = 0,

    // Details
    val name: String,
    val category: String,

    // Inventory and pricing
    val price: Double,
    val stock: Int,

    // Media
    val imageUri: String? = null
)
