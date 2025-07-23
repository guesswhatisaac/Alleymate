package com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.query_results

/**
 * Represents total revenue grouped by year and month.
 */
data class MonthlyRevenue(
    val year: Int,
    val month: Int,
    val totalRevenueInCents: Long
)
