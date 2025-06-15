package com.mobdeve.s18.roman.isaacnathan.alleymate.ui.catalogue

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Archive
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mobdeve.s18.roman.isaacnathan.alleymate.common.components.*
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.CatalogueItem
import com.mobdeve.s18.roman.isaacnathan.alleymate.ui.catalogue.components.*

private sealed interface ModalState {
    data object None : ModalState
    data object AddProduct : ModalState
    data class EditProduct(val item: CatalogueItem) : ModalState
    data class RestockProduct(val item: CatalogueItem) : ModalState
}

@Composable
fun CatalogueScreen(onNavigateToAllocate: () -> Unit) {
    var modalState by remember { mutableStateOf<ModalState>(ModalState.None) }

    when (val state = modalState) {
        is ModalState.None -> { /* Do nothing */ }
        is ModalState.AddProduct -> {
            AddProductModal(
                onDismissRequest = { modalState = ModalState.None },
                onAddProduct = {
                    // TODO: Handle adding the product
                    modalState = ModalState.None
                }
            )
        }
        is ModalState.EditProduct -> {
            EditProductModal(
                item = state.item,
                onDismissRequest = { modalState = ModalState.None },
                onConfirmEdit = { updatedItem ->
                    // TODO: Handle the edit logic
                    println("Saving changes for: $updatedItem")
                    modalState = ModalState.None
                }
            )
        }
        is ModalState.RestockProduct -> {
            RestockProductModal(
                item = state.item,
                onDismissRequest = { modalState = ModalState.None },
                onConfirmRestock = { quantity ->
                    // TODO: Handle the restock logic
                    println("Restocking ${state.item.name} with $quantity items.")
                    modalState = ModalState.None
                }
            )
        }
    }

    Scaffold(
        topBar = {
            AppTopBar(
                title = "Catalogue",
                actions = {
                    BadgedIconButton(
                        badgeCount = 3,
                        icon = Icons.Outlined.Archive,
                        contentDescription = "View Exports",
                        onClick = onNavigateToAllocate
                    )
                }
            )
        },
        floatingActionButton = {
            AppFloatingActionButton(
                onClick = { modalState = ModalState.AddProduct }
            )
        }
    ) { innerPadding ->
        val items = listOf(
            CatalogueItem(1, "MHYLOW star sticker", "Sticker", 100, 50),
            CatalogueItem(2, "MHYLOW star sticker", "Sticker", 100, 50),
            CatalogueItem(3, "MHYLOW star sticker", "Sticker", 100, 50),
            CatalogueItem(4, "Art Print A", "Print", 250, 20),
            CatalogueItem(5, "Art Print B", "Print", 250, 15),
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(top = 8.dp, bottom = 80.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                CategoryFilters()
            }
            item {
                SectionHeader(
                    title = "${items.size} Total Designs",
                    showDivider = true,
                    isSubtle = true,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            val chunkedItems = items.chunked(2)
            items(chunkedItems) { rowItems ->
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(modifier = Modifier.weight(1f)) {
                        CatalogueItemCard(
                            item = rowItems[0],
                            onRestockClick = { modalState = ModalState.RestockProduct(rowItems[0]) },
                            onEditClick = { modalState = ModalState.EditProduct(rowItems[0]) }
                        )
                    }
                    Box(modifier = Modifier.weight(1f)) {
                        if (rowItems.size > 1) {
                            CatalogueItemCard(
                                item = rowItems[1],
                                onRestockClick = { modalState = ModalState.RestockProduct(rowItems[1]) },
                                onEditClick = { modalState = ModalState.EditProduct(rowItems[1]) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CatalogueScreenPreview() {
    CatalogueScreen(onNavigateToAllocate = {})
}