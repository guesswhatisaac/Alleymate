package com.mobdeve.s18.roman.isaacnathan.alleymate.common.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.mobdeve.s18.roman.isaacnathan.alleymate.common.components.AppTopBar
import com.mobdeve.s18.roman.isaacnathan.alleymate.common.components.HomeTopBar

sealed interface Screen {
    val route: String
    val title: String

    // Each screen MUST define how its TopBar should look.
    @Composable
    fun TopBar(navController: NavController)

    // 1. The redundant `StandardTopBar` function has been REMOVED.

    data object Home : Screen {
        override val route: String = "home_screen"
        override val title: String = "Home"

        @Composable
        override fun TopBar(navController: NavController) {
            // Home screen uses the clean, white top bar.
            HomeTopBar(title = title)
        }
    }

    data object Catalogue : Screen {
        override val route: String = "catalogue_screen"
        override val title: String = "Catalogue"

        @Composable
        override fun TopBar(navController: NavController) {
            // Catalogue uses the purple bar AND has a custom action button.
            // 2. THIS SHOWS THE POWER OF THIS PATTERN.
            AppTopBar(
                title = title,
                actions = {
                    IconButton(onClick = { /* TODO: Navigate to Add Item Screen */ }) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add Item",
                            tint = MaterialTheme.colorScheme.onPrimary // Use theme color
                        )
                    }
                }
            )
        }
    }

    data object Events : Screen {
        override val route: String = "events_screen"
        override val title: String = "Events"

        @Composable
        override fun TopBar(navController: NavController) {
            // 3. Events screen directly calls AppTopBar. It's simple and has no actions.
            AppTopBar(title = title)
        }
    }

    data object Reports : Screen {
        override val route: String = "reports_screen"
        override val title: String = "Reports"

        @Composable
        override fun TopBar(navController: NavController) {
            // 4. Reports screen also calls AppTopBar directly.
            AppTopBar(title = title)
        }
    }
}