package com.mobdeve.s18.roman.isaacnathan.alleymate.ui

import androidx.compose.animation.*
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mobdeve.s18.roman.isaacnathan.alleymate.ui.allocate.AllocateScreen
import com.mobdeve.s18.roman.isaacnathan.alleymate.ui.live_sale.LiveSaleScreen

object AppDestinations {

    // main screen
    const val MAIN_TABS_ROUTE = "main_tabs"

    // bottom nav bar screens (used in MainScreen.kt)
    const val HOME_ROUTE = "home_screen"
    const val CATALOGUE_ROUTE = "catalogue_screen"
    const val EVENTS_ROUTE = "events_screen"
    const val REPORTS_ROUTE = "reports_screen"

    // sub screens
    const val ALLOCATE_ROUTE = "allocate_screen"
    const val LIVE_SALE_ROUTE = "live_sale_screen"


}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AppDestinations.MAIN_TABS_ROUTE
    ) {
        composable(route = AppDestinations.MAIN_TABS_ROUTE) {
            MainScreen(
                onNavigateToLiveSale = {
                    navController.navigate(AppDestinations.LIVE_SALE_ROUTE)
                },
                onNavigateToAllocate = {
                    navController.navigate(AppDestinations.ALLOCATE_ROUTE)
                }
            )
        }

        composable(
            route = AppDestinations.LIVE_SALE_ROUTE,
            enterTransition = { slideInVertically(initialOffsetY = { it }) },
            popExitTransition = { slideOutVertically(targetOffsetY = { it }) }
        ) {
            LiveSaleScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = AppDestinations.ALLOCATE_ROUTE,
            enterTransition = { slideInVertically(initialOffsetY = { it }) },
            popExitTransition = { slideOutVertically(targetOffsetY = { it }) }
        ) {
            AllocateScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

    }
}