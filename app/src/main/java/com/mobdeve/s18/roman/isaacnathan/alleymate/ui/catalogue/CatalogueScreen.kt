package com.mobdeve.s18.roman.isaacnathan.alleymate.ui.catalogue

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Archive
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mobdeve.s18.roman.isaacnathan.alleymate.common.components.*
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.CatalogueItem
import com.mobdeve.s18.roman.isaacnathan.alleymate.ui.catalogue.components.CatalogueItemCard
import com.mobdeve.s18.roman.isaacnathan.alleymate.ui.catalogue.components.CategoryFilters

// ========================
// Catalogue Main Screen
// ========================

@Composable
fun CatalogueScreen() {
    val exportCount = 3

    // --- Scaffold Layout: TopBar + FAB ---
    Scaffold(
        topBar = {
            AppTopBar(
                title = "Catalogue",
                actions = {
                    BadgedIconButton(
                        badgeCount = exportCount,
                        icon = Icons.Outlined.Archive,
                        contentDescription = "View Exports",
                        onClick = { /* TODO: Handle export button click */ }
                    )
                }
            )
        },
        floatingActionButton = {
            AppFloatingActionButton(
                onClick = { /* TODO: Navigate to Add Item Screen */ }
            )
        }
    ) { innerPadding ->

        // --- Dummy Data for Preview ---
        val items = listOf(
            CatalogueItem(1, "MHYLOW star sticker", "Sticker", 100, 50),
            CatalogueItem(2, "MHYLOW star sticker", "Sticker", 100, 50),
            CatalogueItem(3, "MHYLOW star sticker", "Sticker", 100, 50),
            CatalogueItem(4, "Art Print A", "Print", 250, 20),
            CatalogueItem(5, "Art Print B", "Print", 250, 15),
            CatalogueItem(6, "Art Print A", "Print", 250, 20),
            CatalogueItem(7, "Art Print B", "Print", 250, 15),
            CatalogueItem(8, "Art Print A", "Print", 250, 20),
            CatalogueItem(9, "Art Print B", "Print", 250, 15),
            CatalogueItem(10, "Art Print A", "Print", 250, 20),
            CatalogueItem(11, "Art Print B", "Print", 250, 15),
        )

        // =======================
        // Screen Content (List)
        // =======================
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(top = 8.dp, bottom = 80.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            // --- Filter Section ---
            item {
                CategoryFilters()
            }

            // --- Section Header ---
            item {
                SectionHeader(
                    title = "${items.size} Total Designs",
                    showDivider = true,
                    isSubtle = true,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            // --- Catalogue Item Grid (2 Columns) ---
            val chunkedItems = items.chunked(2)
            items(chunkedItems) { rowItems ->
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // First item in row
                    Box(modifier = Modifier.weight(1f)) {
                        CatalogueItemCard(item = rowItems[0])
                    }

                    // Second item in row, if present
                    Box(modifier = Modifier.weight(1f)) {
                        if (rowItems.size > 1) {
                            CatalogueItemCard(item = rowItems[1])
                        }
                    }
                }
            }
        }
    }
}

// ==================
// Preview Composable
// ==================

@Preview(showBackground = true)
@Composable
fun CatalogueScreenPreview() {
    CatalogueScreen()
}
