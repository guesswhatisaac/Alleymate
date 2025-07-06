package com.mobdeve.s18.roman.isaacnathan.alleymate.ui

import androidx.compose.animation.*
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mobdeve.s18.roman.isaacnathan.alleymate.ui.allocate.AllocateScreen
import com.mobdeve.s18.roman.isaacnathan.alleymate.ui.live_sale.LiveSaleScreen
import com.mobdeve.s18.roman.isaacnathan.alleymate.ui.events.EventDetailScreen
import androidx.navigation.NavType
import androidx.navigation.navArgument


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

    // sub events
    const val EVENT_DETAIL_ROUTE = "event_detail"
    const val EVENT_ID_ARG = "eventId"

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
                navController = navController,
                onNavigateToLiveSale = {
                    navController.navigate(AppDestinations.LIVE_SALE_ROUTE)
                },
                onNavigateToAllocate = {
                    navController.navigate(AppDestinations.ALLOCATE_ROUTE)
                }
            )
        }

        composable(
            route = "${AppDestinations.EVENT_DETAIL_ROUTE}/{${AppDestinations.EVENT_ID_ARG}}",
            arguments = listOf(navArgument(AppDestinations.EVENT_ID_ARG) { type = NavType.IntType }),
            enterTransition = {
                slideInVertically(initialOffsetY = { it }) // 'it' is the full height of the screen
            },
            popExitTransition = {
                slideOutVertically(targetOffsetY = { it })
            },
        ) { backStackEntry ->
            val eventId = backStackEntry.arguments?.getInt(AppDestinations.EVENT_ID_ARG)

            if (eventId != null) {
                EventDetailScreen(
                    eventId = eventId,

                    onNavigateBack = {
                        if (navController.previousBackStackEntry != null) {
                            navController.popBackStack()
                        }
                    }

                )
            }
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