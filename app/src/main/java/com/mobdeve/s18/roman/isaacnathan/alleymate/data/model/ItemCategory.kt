package com.mobdeve.s18.roman.isaacnathan.alleymate.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "item_categories")

data class ItemCategory(
    @PrimaryKey(autoGenerate = true)
    val itemCategoryId: Int = 0,

    @ColumnInfo(index = true)
    val name: String
)