package com.mobdeve.s18.roman.isaacnathan.alleymate.ui.reports

import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.Event

// Time filters for report generation
enum class TimeFilter(val displayName: String) {
    ALL_TIME("All Time"),
    LAST_7_DAYS("Last 7 Days"),
    LAST_30_DAYS("Last 30 Days"),
    THIS_MONTH("This Month")
}

// Holds the currently selected filters for generating a report
data class ReportsFilterState(
    val selectedEvent: Event?, // Null means "All Events"
    val selectedTimeFilter: TimeFilter = TimeFilter.ALL_TIME
)

// Aggregated report statistics
data class ReportStats(
    val totalRevenueInCents: Long = 0,
    val totalExpensesInCents: Long = 0,
    val netProfitInCents: Long = 0,
    val itemsSoldCount: Int = 0,
    val transactionsCount: Int = 0
)

// Represents a top-selling item in the report
data class BestSeller(
    val rank: Int,
    val name: String,
    val quantitySold: Int,
    val totalRevenueInCents: Long
)
