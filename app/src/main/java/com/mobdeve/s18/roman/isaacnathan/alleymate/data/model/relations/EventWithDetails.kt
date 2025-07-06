package com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.Event
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.EventExpense
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.EventInventoryItem

data class EventWithDetails(
    @Embedded
    val event: Event,

    @Relation(
        entity = EventInventoryItem::class,
        parentColumn = "eventId",
        entityColumn = "eventId"
    )

    val inventory: List<EventInventoryWithDetails>,

    @Relation(
        parentColumn = "eventId",
        entityColumn = "eventId"
    )
    val expenses: List<EventExpense>
)