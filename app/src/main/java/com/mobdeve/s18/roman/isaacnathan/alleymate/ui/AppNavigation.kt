package com.mobdeve.s18.roman.isaacnathan.alleymate.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.mobdeve.s18.roman.isaacnathan.alleymate.ui.allocate.AllocateScreen
import com.mobdeve.s18.roman.isaacnathan.alleymate.ui.events.EventDetailScreen
import com.mobdeve.s18.roman.isaacnathan.alleymate.ui.live_sale.LiveSaleScreen

// Navigation route constants used across the app
object AppDestinations {
    const val MAIN_TABS_ROUTE = "main_tabs" // Root route with bottom nav

    // Bottom navigation routes
    const val HOME_ROUTE = "home_screen"
    const val CATALOGUE_ROUTE = "catalogue_screen"
    const val EVENTS_ROUTE = "events_screen"
    const val REPORTS_ROUTE = "reports_screen"

    // Sub screens
    const val ALLOCATE_ROUTE = "allocate_screen"
    const val LIVE_SALE_ROUTE = "live_sale_screen"

    // Event detail screen
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
        // Main tab screen (contains bottom nav)
        composable(route = AppDestinations.MAIN_TABS_ROUTE) {
            MainScreen(
                navController = navController,
                onNavigateToLiveSale = { eventId ->
                    navController.navigate("${AppDestinations.LIVE_SALE_ROUTE}/$eventId")
                },
                onNavigateToAllocate = {
                    navController.navigate(AppDestinations.ALLOCATE_ROUTE)
                }
            )
        }

        val animationDuration = 400

        // Event Detail Screen with animation and event ID argument
        composable(
            route = "${AppDestinations.EVENT_DETAIL_ROUTE}/{${AppDestinations.EVENT_ID_ARG}}",
            arguments = listOf(navArgument(AppDestinations.EVENT_ID_ARG) { type = NavType.IntType }),
            enterTransition = { slideInVertically(animationSpec = tween(animationDuration), initialOffsetY = { it }) },
            exitTransition = { fadeOut(animationSpec = tween(animationDuration)) },
            popEnterTransition = { fadeIn(animationSpec = tween(animationDuration)) },
            popExitTransition = { slideOutVertically(animationSpec = tween(animationDuration), targetOffsetY = { it }) }
        ) { backStackEntry ->
            val eventId = backStackEntry.arguments?.getInt(AppDestinations.EVENT_ID_ARG)
            if (eventId != null) {
                EventDetailScreen(
                    eventId = eventId,
                    navController = navController,
                    onNavigateBack = {
                        if (navController.previousBackStackEntry != null) {
                            navController.popBackStack()
                        }
                    },
                    onNavigateToLiveSale = { currentEventId ->
                        navController.navigate("${AppDestinations.LIVE_SALE_ROUTE}/$currentEventId")
                    }
                )
            }
        }

        // Live Sale Screen with animation and event ID argument
        composable(
            route = "${AppDestinations.LIVE_SALE_ROUTE}/{${AppDestinations.EVENT_ID_ARG}}",
            arguments = listOf(navArgument(AppDestinations.EVENT_ID_ARG) { type = NavType.IntType }),
            enterTransition = { slideInVertically(animationSpec = tween(animationDuration), initialOffsetY = { it }) },
            exitTransition = { fadeOut(animationSpec = tween(animationDuration)) },
            popEnterTransition = { fadeIn(animationSpec = tween(animationDuration)) },
            popExitTransition = { slideOutVertically(animationSpec = tween(animationDuration), targetOffsetY = { it }) }
        ) { backStackEntry ->
            val eventId = backStackEntry.arguments?.getInt(AppDestinations.EVENT_ID_ARG)
            if (eventId != null) {
                LiveSaleScreen(
                    eventId = eventId,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }

        // Allocate Screen with animation
        composable(
            route = AppDestinations.ALLOCATE_ROUTE,
            enterTransition = { slideInVertically(animationSpec = tween(animationDuration), initialOffsetY = { it }) },
            exitTransition = { fadeOut(animationSpec = tween(animationDuration)) },
            popEnterTransition = { fadeIn(animationSpec = tween(animationDuration)) },
            popExitTransition = { slideOutVertically(animationSpec = tween(animationDuration), targetOffsetY = { it }) }
        ) {
            AllocateScreen(onNavigateBack = { navController.popBackStack() })
        }
    }
}
