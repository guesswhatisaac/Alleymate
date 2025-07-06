package com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.CatalogueItem
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.EventInventoryItem


data class EventInventoryWithDetails(
    @Embedded
    val eventInventoryItem: EventInventoryItem,

    @Relation(
        parentColumn = "itemId", // CatalogueItem's primary key
        entityColumn = "itemId"  // EventInventoryItem's foreign key
    )
    val catalogueItem: CatalogueItem
)