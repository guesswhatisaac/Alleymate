package com.mobdeve.s18.roman.isaacnathan.alleymate.ui.catalogue

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Archive
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mobdeve.s18.roman.isaacnathan.alleymate.common.components.*
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.CatalogueItem
import com.mobdeve.s18.roman.isaacnathan.alleymate.ui.catalogue.components.*
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.Color
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.AllocationStateHolder
import com.mobdeve.s18.roman.isaacnathan.alleymate.ui.catalogue.components.DeleteCategoryModal

private sealed interface ModalState {
    data object None : ModalState
    data object AddProduct : ModalState
    data object AddCategory : ModalState
    data class EditProduct(val item: CatalogueItem) : ModalState
    data class RestockProduct(val item: CatalogueItem) : ModalState
    data class DeleteConfirmation(val item: CatalogueItem) : ModalState
    data class DeleteCategory(val category: String) : ModalState
}

@Composable
fun CatalogueScreen(
    onNavigateToAllocate: () -> Unit,
    viewModel: CatalogueViewModel = viewModel()
) {
    var modalState by remember { mutableStateOf<ModalState>(ModalState.None) }

    val itemCategories by viewModel.itemCategories.collectAsState()
    val selectedItemCategory by viewModel.selectedItemCategory.collectAsState()
    var navigationTriggered by remember { mutableStateOf(false) }

    val lazyListState = rememberSaveable(saver = LazyListState.Saver) {
        LazyListState()
    }

    val allItemCategories by viewModel.itemCategories.collectAsState()
    val modalCategories = allItemCategories.filter { it != "ALL" }

    val items by viewModel.filteredItems.collectAsState()
    val categoryItemCount by viewModel.categoryItemCount.collectAsState()
    val selectedItemIds by viewModel.selectedItemIds.collectAsState()
    val inSelectionMode by viewModel.inSelectionMode.collectAsState()
    val allocationBadgeCount by viewModel.allocationBadgeCount.collectAsState()

    LaunchedEffect(Unit) {
        navigationTriggered = false
    }

    when (val state = modalState) {
        is ModalState.None -> { /* Do nothing */ }
        is ModalState.AddProduct -> {
            AddProductModal(
                itemCategories = modalCategories,
                onDismissRequest = { modalState = ModalState.None },
                onAddProduct = { name, category, price, stock, imageUri ->
                    viewModel.addProduct(name, category, price, stock, imageUri)
                    modalState = ModalState.None
                }
            )
        }
        is ModalState.AddCategory -> {
            AddCategoryModal(
                onDismissRequest = { modalState = ModalState.None },
                onAddCategory = { categoryName ->
                    viewModel.addItemCategory(categoryName)
                    modalState = ModalState.None
                }
            )
        }
        is ModalState.EditProduct -> {
            EditProductModal(
                item = state.item,
                itemCategories = modalCategories,
                onDismissRequest = { modalState = ModalState.None },
                onConfirmEdit = { updatedItem ->
                    viewModel.editProduct(updatedItem)
                    modalState = ModalState.None
                }
            )
        }
        is ModalState.RestockProduct -> {
            RestockProductModal(
                item = state.item,
                onDismissRequest = { modalState = ModalState.None },
                onConfirmRestock = { quantity ->
                    viewModel.restockProduct(state.item, quantity)
                    modalState = ModalState.None
                }
            )
        }
        is ModalState.DeleteConfirmation -> {
            DeleteCatalogueItemModal(
                item = state.item,
                onDismissRequest = { modalState = ModalState.None },
                onConfirmDelete = {
                    viewModel.deleteItem(state.item)
                    modalState = ModalState.None
                }
            )
        }
        is ModalState.DeleteCategory -> {
            val isDeletable = categoryItemCount == 0
            DeleteCategoryModal(
                categoryName = state.category,
                isDeletable = isDeletable,
                itemCount = categoryItemCount,
                onDismissRequest = { modalState = ModalState.None },
                onConfirmDelete = {
                    viewModel.deleteCategory(state.category)
                    modalState = ModalState.None
                }
            )
        }
    }

    Scaffold(
        topBar = {
            AppTopBar(
                title = if (inSelectionMode) {
                    "$allocationBadgeCount Selected"
                } else {
                    "Catalogue"
                },
                navigationIcon = {
                    if (inSelectionMode) {
                        IconButton(onClick = viewModel::clearSelection) {
                            Icon(Icons.Default.Close, tint = Color.White, contentDescription = "Clear selection")
                        }
                    }
                },
                actions = {
                    if (inSelectionMode) {
                        IconButton(onClick = {
                            viewModel.prepareForAllocation()
                        }) {
                            Icon(Icons.Default.Check, contentDescription = "Confirm Selection")
                        }
                    } else {
                        BadgedIconButton(
                            badgeCount = AllocationStateHolder.getAllocationCount(),
                            icon = Icons.Outlined.Archive,
                            contentDescription = "View Allocations",
                            onClick = {
                                if(!navigationTriggered){
                                    navigationTriggered = true
                                    onNavigateToAllocate()
                                }
                            }
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            if (!inSelectionMode) {
                AppFloatingActionButton(
                    onClick = { modalState = ModalState.AddProduct }
                )
            }
        }
    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            state = lazyListState,
            contentPadding = PaddingValues(top = 8.dp, bottom = 80.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Header 1: Category Filters
            item {
                CategoryFilters(
                    categories = itemCategories,
                    selectedCategory = selectedItemCategory,
                    onCategorySelected = { category ->
                        viewModel.selectItemCategory(category)
                        viewModel.updateCategoryItemCount(category)
                    },
                    onAddCategoryClicked = { modalState = ModalState.AddCategory },
                    // UPDATED: Use the new long press callback
                    onCategoryLongPress = { category ->
                        viewModel.updateCategoryItemCount(category)
                        modalState = ModalState.DeleteCategory(category)
                    }
                )
            }

            // Header 2: Section Header
            item {
                SectionHeader(
                    title = "${items.size} Total Designs",
                    showDivider = true,
                    isSubtle = true,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            // Conditional Content: Either the empty message OR the grid of items.
            if (items.isEmpty()) {
                item {
                    EmptyStateMessage(
                        title = "Your Catalogue is Empty",
                        subtitle = "Tap the '+' button to add your first product.",
                        titleColor = Color.Black,
                        subtitleColor = Color.Gray
                    )
                }
            } else {
                val chunkedItems = items.chunked(2)
                items(
                    items = chunkedItems,
                    key = { row -> row.joinToString { it.itemId.toString() } }
                ) { rowItems ->
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // --- Logic for the first card in the row ---
                        Box(modifier = Modifier.weight(1f)) {
                            val item = rowItems[0]
                            val isSelected = item.itemId in selectedItemIds
                            CatalogueItemCard(
                                item = item,
                                isSelected = isSelected,
                                onLongClick = { viewModel.toggleSelection(item.itemId) },
                                onClick = { if (inSelectionMode) viewModel.toggleSelection(item.itemId) },
                                onRestockClick = { modalState = ModalState.RestockProduct(item) },
                                onEditClick = { modalState = ModalState.EditProduct(item) },
                                onDeleteClick = { modalState = ModalState.DeleteConfirmation(item) }                            )
                        }

                        // --- Logic for the second card in the row (if it exists) ---
                        Box(modifier = Modifier.weight(1f)) {
                            if (rowItems.size > 1) {
                                val item = rowItems[1]
                                val isSelected = item.itemId in selectedItemIds
                                CatalogueItemCard(
                                    item = item,
                                    isSelected = isSelected,
                                    onLongClick = { viewModel.toggleSelection(item.itemId) },
                                    onClick = { if (inSelectionMode) viewModel.toggleSelection(item.itemId) },
                                    onRestockClick = { modalState = ModalState.RestockProduct(item) },
                                    onEditClick = { modalState = ModalState.EditProduct(item) },
                                    onDeleteClick = { modalState = ModalState.DeleteConfirmation(item) }                            )

                            }
                        }
                    }
                }
            }
        }
    }
}