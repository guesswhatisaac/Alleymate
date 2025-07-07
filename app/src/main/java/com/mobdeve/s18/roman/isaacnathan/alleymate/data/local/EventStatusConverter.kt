package com.mobdeve.s18.roman.isaacnathan.alleymate.data.local

import androidx.room.TypeConverter
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.EventStatus

class EventStatusConverter {
    @TypeConverter
    fun fromStatus(status: EventStatus): String {
        return status.name
    }

    @TypeConverter
    fun toStatus(name: String): EventStatus {
        return EventStatus.valueOf(name)
    }
}