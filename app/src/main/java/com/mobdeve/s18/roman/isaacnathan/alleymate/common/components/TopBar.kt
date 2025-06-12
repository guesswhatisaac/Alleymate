package com.mobdeve.s18.roman.isaacnathan.alleymate.common.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextOverflow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(
    title: String,
    actions: @Composable () -> Unit = {}
) {
    TopAppBar(
        title = {
            Text(
                text = title.uppercase(),
                style = MaterialTheme.typography.headlineMedium,
                // Best Practice: Text on a 'surface' color should use the 'onSurface' color from the theme.
                color = MaterialTheme.colorScheme.primary
            )
        },
        actions = { actions() },
        colors = TopAppBarDefaults.topAppBarColors(
            // Correctly uses a neutral background for the home screen
            containerColor = MaterialTheme.colorScheme.surface
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar( // 1. Renamed for better reusability and convention
    title: String,
    actions: @Composable () -> Unit = {}
    // 2. The onNavigateUp parameter is removed since there's no back button
) {
    TopAppBar(
        title = {
            Text(
                text = title.uppercase(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.headlineMedium,
                // Best Practice: Text on a 'primary' color should use the 'onPrimary' color
                color = MaterialTheme.colorScheme.onPrimary
            )
        },
        // NOTE: There is no 'navigationIcon' here, so no back button will be shown. This is correct per your request.
        actions = { actions() },
        colors = TopAppBarDefaults.topAppBarColors(
            // Correctly uses the main brand color for other screens
            containerColor = MaterialTheme.colorScheme.primary
        )
    )
}