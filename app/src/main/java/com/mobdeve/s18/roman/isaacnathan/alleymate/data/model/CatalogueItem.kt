package com.mobdeve.s18.roman.isaacnathan.alleymate.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "catalogue_items")
data class CatalogueItem(
    @PrimaryKey(autoGenerate = true)
    val itemId: Int = 0,
    val name: String,
    val category: String,
    val price: Double,
    val stock: Int,
    val imageUri: String? = null
)