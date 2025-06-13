package com.mobdeve.s18.roman.isaacnathan.alleymate.data.model

enum class EventStatus {
    LIVE, UPCOMING, ENDED
}

data class Event(
    val id: Int,
    val title: String,
    val date: String,
    val location: String,
    val status: EventStatus,
    val itemsAllocated: Int,
    val itemsSold: Int,
    val expenses: Int,
    val profit: Int,
)