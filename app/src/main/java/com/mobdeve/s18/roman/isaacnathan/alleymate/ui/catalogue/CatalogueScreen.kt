package com.mobdeve.s18.roman.isaacnathan.alleymate.ui.catalogue

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mobdeve.s18.roman.isaacnathan.alleymate.common.components.AppTopBar
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.CatalogueItem
import com.mobdeve.s18.roman.isaacnathan.alleymate.ui.catalogue.components.CatalogueItemCard
import com.mobdeve.s18.roman.isaacnathan.alleymate.ui.catalogue.components.CategoryFilters
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import com.mobdeve.s18.roman.isaacnathan.alleymate.common.components.SectionHeader

@Composable
fun CatalogueScreen() {
    Scaffold(
        topBar = {
            AppTopBar(title = "Catalogue")
        }
    ) { innerPadding ->

        val items = listOf(
            CatalogueItem(1, "MHYLOW star sticker", "Sticker", 100, 50),
            CatalogueItem(2, "MHYLOW star sticker", "Sticker", 100, 50),
            CatalogueItem(3, "MHYLOW star sticker", "Sticker", 100, 50),
            CatalogueItem(4, "Art Print A", "Print", 250, 20),
            CatalogueItem(5, "Art Print B", "Print", 250, 15),
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // === Chip Category Filters
            item(span = { GridItemSpan(maxLineSpan) }) {
                CategoryFilters()
            }

            // === Divider & Count
            item(span = { GridItemSpan(maxLineSpan) }) {
                SectionHeader(
                    title = "${items.size} Total Designs",
                    showDivider = true,
                    isSubtle = true,
                )
            }

            // Grid Items
            items(items) { item ->
                CatalogueItemCard(item = item)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CatalogueScreenPreview() {
    CatalogueScreen()
}