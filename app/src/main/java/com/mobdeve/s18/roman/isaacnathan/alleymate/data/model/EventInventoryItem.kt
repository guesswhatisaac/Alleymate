package com.mobdeve.s18.roman.isaacnathan.alleymate.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "event_inventory",
    primaryKeys = ["eventId", "itemId"],
    foreignKeys = [
        ForeignKey(
            entity = Event::class,
            parentColumns = ["eventId"],
            childColumns = ["eventId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = CatalogueItem::class,
            parentColumns = ["itemId"],
            childColumns = ["itemId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["itemId"])]

)
data class EventInventoryItem(
    val eventId: Int,
    val itemId: Int,

    val allocatedQuantity: Int,
    val soldQuantity: Int = 0
)