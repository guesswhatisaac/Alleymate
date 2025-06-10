package com.mobdeve.s18.roman.isaacnathan.alleymate.ui.screenHomepage


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mobdeve.s18.roman.isaacnathan.alleymate.theme.AlleyMateTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage() {
    // State to keep track of the selected bottom navigation item
    var selectedItemIndex by remember { mutableIntStateOf(0) }
    val bottomNavItems = listOf("Home", "Favorites", "Profile")
    val bottomNavIcons = listOf(Icons.Filled.Home, Icons.Filled.Favorite, Icons.Filled.Person)

    // Scaffold provides the basic Material Design layout structure.
    // It handles the positioning of AppBars, FABs, and content.
    // The `content` lambda receives padding values that account for the
    // space taken by the top and bottom bars, which is how we achieve
    // a safe edge-to-edge layout.
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "AlleyMate",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            )
        },
        bottomBar = {
            NavigationBar {
                bottomNavItems.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = selectedItemIndex == index,
                        onClick = { selectedItemIndex = index },
                        label = { Text(text = item) },
                        icon = {
                            Icon(
                                imageVector = bottomNavIcons[index],
                                contentDescription = "$item Icon"
                            )
                        }
                    )
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { /* TODO: Handle Add Action */ }) {
                Icon(Icons.Filled.Add, contentDescription = "Add")
            }
        }
    ) { innerPadding ->
        // Main content of the screen
        // The `innerPadding` should be applied here to avoid content
        // being drawn behind the top and bottom bars.
        HomeScreenContent(
            paddingValues = innerPadding
        )
    }
}

@Composable
fun HomeScreenContent(paddingValues: PaddingValues) {
    // A LazyColumn is the Compose equivalent of a RecyclerView.
    // It's efficient for displaying long, scrollable lists.
    val sampleData = (1..20).map { "Bowling Session #$it" }

    LazyColumn(
        modifier = Modifier
            .padding(paddingValues) // Apply the padding from the Scaffold
            .padding(horizontal = 16.dp), // Add some extra horizontal padding
        verticalArrangement = Arrangement.spacedBy(8.dp) // Space between items
    ) {
        // A header item
        item {
            Text(
                text = "Recent Activity",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(vertical = 16.dp)
            )
        }

        // The list of items
        items(sampleData) { data ->
            ContentCard(text = data)
        }
    }
}

@Composable
fun ContentCard(text: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(16.dp)
        )
    }
}


@Preview(showBackground = true)
@Composable
fun HomePagePreview() {
    AlleyMateTheme {
        HomePage()
    }
}