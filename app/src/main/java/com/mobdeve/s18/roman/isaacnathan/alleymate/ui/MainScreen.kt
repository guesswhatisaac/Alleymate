package com.mobdeve.s18.roman.isaacnathan.alleymate.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.mobdeve.s18.roman.isaacnathan.alleymate.common.components.BottomNavBar
import com.mobdeve.s18.roman.isaacnathan.alleymate.common.components.TopBar
import com.mobdeve.s18.roman.isaacnathan.alleymate.common.navigation.Screen
import com.mobdeve.s18.roman.isaacnathan.alleymate.ui.catalogue.CatalogueScreen
import com.mobdeve.s18.roman.isaacnathan.alleymate.ui.events.EventsScreen
import com.mobdeve.s18.roman.isaacnathan.alleymate.ui.home.HomeScreen
import com.mobdeve.s18.roman.isaacnathan.alleymate.ui.reports.ReportsScreen

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val screens = listOf(
        Screen.Home,
        Screen.Catalogue,
        Screen.Events,
        Screen.Reports
    )

    val currentScreen = screens.find { it.route == currentRoute } ?: Screen.Home

    Scaffold(
        topBar = {
            TopBar(title = currentScreen.title)
        },
        bottomBar = {
            BottomNavBar(
                currentRoute = currentRoute,
                onItemSelected = { route ->
                    navController.navigate(route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            // Your composable routes remain the same
            composable(Screen.Home.route) { HomeScreen() }
            composable(Screen.Catalogue.route) { CatalogueScreen() }
            composable(Screen.Events.route) { EventsScreen() }
            composable(Screen.Reports.route) { ReportsScreen() }
        }
    }
}