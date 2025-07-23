package com.mobdeve.s18.roman.isaacnathan.alleymate.data.local

import androidx.room.TypeConverter
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.EventStatus

class EventStatusConverter {

    // Converts EventStatus to String for Room storage
    @TypeConverter
    fun fromStatus(status: EventStatus): String {
        return status.name
    }

    // Converts stored String back to EventStatus enum
    @TypeConverter
    fun toStatus(name: String): EventStatus {
        return EventStatus.valueOf(name)
    }
}
