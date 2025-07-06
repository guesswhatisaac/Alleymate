package com.mobdeve.s18.roman.isaacnathan.alleymate.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "event_expenses",
    foreignKeys = [
        ForeignKey(
            entity = Event::class,
            parentColumns = ["eventId"],
            childColumns = ["eventId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["eventId"])]

)
data class EventExpense(
    @PrimaryKey(autoGenerate = true)
    val expenseId: Int = 0,
    val eventId: Int,
    val description: String,
    val amountInCents: Long
)