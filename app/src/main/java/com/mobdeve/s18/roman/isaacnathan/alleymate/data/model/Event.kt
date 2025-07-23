package com.mobdeve.s18.roman.isaacnathan.alleymate.data.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.mobdeve.s18.roman.isaacnathan.alleymate.ui.events.EventUiModel
import java.text.SimpleDateFormat
import java.util.Locale

enum class EventStatus {
    LIVE, UPCOMING, ENDED
}

// Represents a single event and its related metrics.
@Entity(tableName = "events")
data class Event(

    // Identification
    @PrimaryKey(autoGenerate = true)
    val eventId: Int = 0,

    // Basic details
    val title: String,
    val location: String,

    // Timing
    val startDate: Long,
    val endDate: Long,
    val status: EventStatus = EventStatus.UPCOMING

) {

    // Runtime-only metrics
    @Ignore var totalItemsAllocated: Int = 0
    @Ignore var totalItemsSold: Int = 0
    @Ignore var totalExpensesInCents: Long = 0
    @Ignore var totalRevenueInCents: Long = 0
    @Ignore var totalStockLeft: Int = 0
    @Ignore var catalogueCount: Int = 0

    // Derived values
    @get:Ignore
    val profitInCents: Long
        get() = totalRevenueInCents - totalExpensesInCents

    @get:Ignore
    val dateRangeString: String
        get() {
            val startDateStr = SimpleDateFormat("MMM dd", Locale.getDefault()).format(startDate)
            val endDateStr = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(endDate)
            return if (startDate == endDate) endDateStr else "$startDateStr - $endDateStr"
        }

    // Mapping
    fun toUiModel(): EventUiModel {
        return EventUiModel(
            eventId = this.eventId,
            title = this.title,
            location = this.location,
            startDate = this.startDate,
            endDate = this.endDate,
            status = this.status,
            catalogueCount = this.catalogueCount,
            totalItemsAllocated = this.totalItemsAllocated,
            totalItemsSold = this.totalItemsSold,
            totalStockLeft = this.totalStockLeft,
            totalExpensesInCents = this.totalExpensesInCents,
            totalRevenueInCents = this.totalRevenueInCents
        )
    }
}
