package com.mobdeve.s18.roman.isaacnathan.alleymate.ui

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.mobdeve.s18.roman.isaacnathan.alleymate.common.components.BottomNavBar
import com.mobdeve.s18.roman.isaacnathan.alleymate.ui.catalogue.CatalogueScreen
import com.mobdeve.s18.roman.isaacnathan.alleymate.ui.events.EventsScreen
import com.mobdeve.s18.roman.isaacnathan.alleymate.ui.home.HomeScreen
import com.mobdeve.s18.roman.isaacnathan.alleymate.ui.reports.ReportsScreen
import androidx.navigation.NavHostController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mobdeve.s18.roman.isaacnathan.alleymate.ui.events.EventViewModel

@Composable
fun MainScreen(
    navController: NavHostController,
    onNavigateToLiveSale: (eventId: Int) -> Unit,
    onNavigateToAllocate: () -> Unit
) {
    val tabNavController = rememberNavController()
    val navBackStackEntry by tabNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val eventViewModel: EventViewModel = viewModel()

    // Avoids re-navigating to the same tab
    val navigateToTab = { route: String ->
        if (route != currentRoute) {
            tabNavController.navigate(route) {
                popUpTo(tabNavController.graph.startDestinationId) {
                    saveState = true // preserve screen state on back stack
                }
                launchSingleTop = true // avoid duplicate destinations
                restoreState = true // reload previously saved state
            }
        }
    }

    Scaffold(
        bottomBar = {
            BottomNavBar(
                currentRoute = currentRoute,
                onItemSelected = { route -> navigateToTab(route) }
            )
        },
        contentWindowInsets = WindowInsets(0.dp, 0.dp, 0.dp, 0.dp),
    ) { innerPadding ->
        NavHost(
            navController = tabNavController,
            startDestination = AppDestinations.HOME_ROUTE,
            modifier = Modifier.padding(innerPadding),
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None }
        ) {
            // Main screen showing overview and navigation to events or specific event
            composable(AppDestinations.HOME_ROUTE) {
                HomeScreen(
                    onNavigateToEvents = { navigateToTab(AppDestinations.EVENTS_ROUTE) },
                    onNavigateToLiveSale = onNavigateToLiveSale,
                    onNavigateToEventDetail = { eventId ->
                        navController.navigate("${AppDestinations.EVENT_DETAIL_ROUTE}/$eventId")
                    }
                )
            }

            // Catalogue tab
            composable(AppDestinations.CATALOGUE_ROUTE) {
                CatalogueScreen(onNavigateToAllocate = onNavigateToAllocate)
            }

            // Events tab with shared ViewModel
            composable(AppDestinations.EVENTS_ROUTE) {
                EventsScreen(
                    onNavigateToEventDetail = { eventId ->
                        navController.navigate("${AppDestinations.EVENT_DETAIL_ROUTE}/$eventId")
                    },
                    onNavigateToLiveSale = onNavigateToLiveSale,
                    viewModel = eventViewModel
                )
            }

            // Reports tab
            composable(AppDestinations.REPORTS_ROUTE) {
                ReportsScreen()
            }
        }
    }
}
