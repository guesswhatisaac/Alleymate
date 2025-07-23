package com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.CatalogueItem
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.EventInventoryItem

/**
 * Represents an inventory item for a specific event, joined with its full catalogue item details.
 */
data class EventInventoryWithDetails(
    @Embedded
    val eventInventoryItem: EventInventoryItem,

    @Relation(
        parentColumn = "itemId", // EventInventoryItem's FK
        entityColumn = "itemId"  // CatalogueItem's PK
    )
    val catalogueItem: CatalogueItem
)
