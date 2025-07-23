package com.mobdeve.s18.roman.isaacnathan.alleymate.ui.home

data class HomeOverviewStats(
    val last30DaysRevenueInCents: Long = 0,
    val bestSellerName: String = "N/A",
    val lowStockItemsCount: Int = 0,
    val catalogueItemsCount: Int = 0
)
