package com.mobdeve.s18.roman.isaacnathan.alleymate.ui.events

import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.EventStatus
import java.text.SimpleDateFormat
import java.util.Locale

// UI-friendly representation of an event with extra computed values
data class EventUiModel(
    val eventId: Int,
    val title: String,
    val location: String,
    val startDate: Long,
    val endDate: Long,
    val status: EventStatus,
    val totalItemsAllocated: Int,
    val totalItemsSold: Int,
    val totalExpensesInCents: Long,
    val totalRevenueInCents: Long,
    val totalStockLeft: Int,
    val catalogueCount: Int
) {

    // Readable date range for display
    val dateRangeString: String
        get() {
            val startDateStr = SimpleDateFormat("MMM dd", Locale.getDefault()).format(startDate)
            val endDateStr = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(endDate)
            return if (startDate == endDate) endDateStr else "$startDateStr - $endDateStr"
        }
}
