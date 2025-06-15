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

@Composable
fun MainScreen(
    onNavigateToLiveSale: () -> Unit,
    onNavigateToAllocate: () -> Unit
) {
    val tabNavController = rememberNavController()
    val navBackStackEntry by tabNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            BottomNavBar(
                currentRoute = currentRoute,
                onItemSelected = { route ->
                    tabNavController.navigate(route) {
                        popUpTo(tabNavController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = false
                    }
                }
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
            composable(AppDestinations.HOME_ROUTE) {
                HomeScreen(
                    onNavigateToEvents = { tabNavController.navigate(AppDestinations.EVENTS_ROUTE) },
                    onNavigateToLiveSale = onNavigateToLiveSale
                )
            }
            composable(AppDestinations.CATALOGUE_ROUTE) {
                CatalogueScreen(
                    onNavigateToAllocate = onNavigateToAllocate
                )
            }
            composable(AppDestinations.EVENTS_ROUTE) { EventsScreen() }
            composable(AppDestinations.REPORTS_ROUTE) { ReportsScreen() }
        }
    }
}