package com.mobdeve.s18.roman.isaacnathan.alleymate.common.navigation

sealed class Screen(val route: String, val title: String) {
    data object Home : Screen("home_screen", "Alleymate")
    data object Catalogue : Screen("catalogue_screen", "Catalogue")
    data object Events : Screen("events_screen", "Events")
    data object Reports : Screen("reports_screen", "Reports")
}