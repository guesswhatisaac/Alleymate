// MainScreen.kt

package com.mobdeve.s18.roman.isaacnathan.alleymate.ui

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
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
import com.mobdeve.s18.roman.isaacnathan.alleymate.common.navigation.Screen
import com.mobdeve.s18.roman.isaacnathan.alleymate.theme.AlleyMateTheme
import com.mobdeve.s18.roman.isaacnathan.alleymate.ui.catalogue.CatalogueScreen
import com.mobdeve.s18.roman.isaacnathan.alleymate.ui.events.EventsScreen
import com.mobdeve.s18.roman.isaacnathan.alleymate.ui.home.HomeScreen
import com.mobdeve.s18.roman.isaacnathan.alleymate.ui.reports.ReportsScreen
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.ui.unit.dp

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route


    AlleyMateTheme {
        Scaffold(
            bottomBar = {
                BottomNavBar(
                    currentRoute = currentRoute,
                    onItemSelected = { route ->
                        navController.navigate(route) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )

            },
            contentWindowInsets = WindowInsets(0.dp, 0.dp, 0.dp, 0.dp),
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = Screen.Home.route,
                modifier = Modifier.padding(innerPadding)
            ) {
                val noEnterTransition: EnterTransition = EnterTransition.None
                val noExitTransition: ExitTransition = ExitTransition.None

                composable(
                    route = Screen.Home.route,
                    enterTransition = { noEnterTransition },
                    exitTransition = { noExitTransition }
                ) { HomeScreen() }

                composable(
                    route = Screen.Catalogue.route,
                    enterTransition = { noEnterTransition },
                    exitTransition = { noExitTransition }
                ) { CatalogueScreen() }

                composable(
                    route = Screen.Events.route,
                    enterTransition = { noEnterTransition },
                    exitTransition = { noExitTransition }
                ) { EventsScreen() }

                composable(
                    route = Screen.Reports.route,
                    enterTransition = { noEnterTransition },
                    exitTransition = { noExitTransition }
                ) { ReportsScreen() }
            }
        }
    }
}