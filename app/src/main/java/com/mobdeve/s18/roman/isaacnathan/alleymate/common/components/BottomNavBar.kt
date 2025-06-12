package com.mobdeve.s18.roman.isaacnathan.alleymate.common.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Assessment
import androidx.compose.material.icons.outlined.Event
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Inbox
import androidx.compose.material.icons.rounded.Assessment
import androidx.compose.material.icons.rounded.Event
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Inbox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults // <-- 1. Import this
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color // <-- 2. Import this
import androidx.compose.ui.graphics.vector.ImageVector
import com.mobdeve.s18.roman.isaacnathan.alleymate.common.navigation.Screen
import androidx.compose.material3.MaterialTheme

data class BottomNavItem(
    val label: String,
    val route: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

@Composable
fun BottomNavBar(
    currentRoute: String?,
    onItemSelected: (String) -> Unit
) {
    val items = listOf(
        BottomNavItem("Home", Screen.Home.route, Icons.Rounded.Home, Icons.Outlined.Home),
        BottomNavItem("Catalogue", Screen.Catalogue.route, Icons.Rounded.Inbox, Icons.Outlined.Inbox),
        BottomNavItem("Events", Screen.Events.route, Icons.Rounded.Event, Icons.Outlined.Event),
        BottomNavItem("Reports", Screen.Reports.route, Icons.Rounded.Assessment, Icons.Outlined.Assessment)
    )

    NavigationBar(
        containerColor = Color.Transparent
    ) {
        items.forEach { item ->
            val isSelected = currentRoute == item.route
            NavigationBarItem(
                selected = isSelected,
                onClick = { onItemSelected(item.route) },
                icon = {
                    Icon(
                        imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                        contentDescription = "${item.label} Icon"
                    )
                },
                // 4. Use the `colors` parameter to control item colors
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}

