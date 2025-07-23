package com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.query_results

/**
 * Represents sales performance of a catalogue item.
 */
data class SalesData(
    val itemId: Int,
    val name: String,
    val quantitySold: Int,
    val totalRevenueInCents: Long
)
