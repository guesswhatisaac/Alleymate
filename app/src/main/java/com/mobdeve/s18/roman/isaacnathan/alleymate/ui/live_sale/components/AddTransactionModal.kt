package com.mobdeve.s18.roman.isaacnathan.alleymate.ui.live_sale.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.mobdeve.s18.roman.isaacnathan.alleymate.common.components.LiveSaleTopBar
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.relations.EventInventoryWithDetails
import com.mobdeve.s18.roman.isaacnathan.alleymate.theme.AlleyMainOrange
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.core.net.toUri
import coil.compose.AsyncImage
import com.mobdeve.s18.roman.isaacnathan.alleymate.common.components.AppCard
import com.mobdeve.s18.roman.isaacnathan.alleymate.common.components.modal.QuantityStepper

@Composable
fun AddTransactionModal(
    inventory: List<EventInventoryWithDetails>,
    selectedItemIds: Set<Int>,
    transactionCart: Map<Int, Int>,
    isTransacting: Boolean,
    onDismiss: () -> Unit,
    onItemSelect: (Int) -> Unit,
    onProceed: () -> Unit,
    onQuantityChange: (itemId: Int, quantity: Int) -> Unit,
    onConfirmTransaction: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnClickOutside = false
        )
    ) {
        Scaffold(
            topBar = {
                LiveSaleTopBar(
                    title = if (isTransacting) "Transact" else "Select Items",
                    onNavigateBack = onDismiss
                )
            },
            modifier = Modifier.fillMaxSize()
        ) { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                if (isTransacting) {
                    TransactStep(
                        inventory = inventory,
                        cart = transactionCart,
                        onQuantityChange = onQuantityChange,
                        onConfirm = onConfirmTransaction,

                    )
                } else {
                    SelectStep(
                        inventory = inventory,
                        selectedIds = selectedItemIds,
                        onItemSelect = onItemSelect,
                        onProceed = onProceed
                    )
                }
            }
        }
    }
}


@Composable
private fun SelectStep(
    inventory: List<EventInventoryWithDetails>,
    selectedIds: Set<Int>,
    onItemSelect: (Int) -> Unit,
    onProceed: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            items(items = inventory, key = { it.eventInventoryItem.itemId }) { item ->
                SelectableItemCard(
                    inventoryItem = item,
                    isSelected = item.catalogueItem.itemId in selectedIds,
                    onClick = { onItemSelect(item.catalogueItem.itemId) }
                )
            }
        }

        Button(
            onClick = onProceed,
            enabled = selectedIds.isNotEmpty(),
            modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter).height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = AlleyMainOrange)
        ) {
            Text("NEXT", fontWeight = FontWeight.Bold)
            Spacer(Modifier.width(8.dp))
            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Next")
        }
    }
}


@Composable
private fun TransactStep(
    inventory: List<EventInventoryWithDetails>,
    cart: Map<Int, Int>,
    onQuantityChange: (itemId: Int, quantity: Int) -> Unit,
    onConfirm: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        val itemsInCart = inventory.filter { it.catalogueItem.itemId in cart.keys }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 120.dp)
        ) {
            items(items = itemsInCart, key = { it.eventInventoryItem.itemId }) { item ->

                val currentQuantity = cart[item.catalogueItem.itemId] ?: 0

                TransactReviewCard(
                    item = item,
                    initialQuantity = currentQuantity,
                    onQuantityChange = { newQuantity ->
                        onQuantityChange(item.catalogueItem.itemId, newQuantity)
                    }
                )
            }
        }

        Column(modifier = Modifier.align(Alignment.BottomCenter)) {
            val total = itemsInCart.sumOf {
                (cart[it.catalogueItem.itemId] ?: 0) * it.catalogueItem.price
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = AlleyMainOrange.copy(alpha = 0.1f)
                ),
                border = BorderStroke(1.dp, AlleyMainOrange.copy(alpha = 0.3f)),
                shape = RoundedCornerShape(12.dp)
            ) {
                TransactionTotalRow(totalAmount = total)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = onConfirm,
                enabled = cart.values.any { it > 0 },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AlleyMainOrange)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Confirm Transaction")
                Spacer(Modifier.width(8.dp))
                Text("CONFIRM TRANSACTION", fontWeight = FontWeight.Bold)
            }
        }
    }
}


// ====================================================================================
//  NEW AND MODIFIED HELPER COMPOSABLE
// ====================================================================================


@Composable
private fun TransactReviewCard(
    item: EventInventoryWithDetails,
    initialQuantity: Int,
    onQuantityChange: (Int) -> Unit
) {
    val catalogueItem = item.catalogueItem
    val stockLeft = item.eventInventoryItem.allocatedQuantity - item.eventInventoryItem.soldQuantity

    AppCard {
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            if (catalogueItem.imageUri.isNullOrBlank()) {
                Icon(
                    imageVector = Icons.Outlined.Image,
                    contentDescription = "Image Placeholder",
                    modifier = Modifier.size(64.dp).clip(MaterialTheme.shapes.medium),
                    tint = Color(0xFFC9C3CD)
                )
            } else {
                AsyncImage(
                    model = catalogueItem.imageUri.toUri(),
                    contentDescription = "${catalogueItem.name} image",
                    modifier = Modifier.size(64.dp).clip(MaterialTheme.shapes.medium),
                    contentScale = ContentScale.Crop
                )
            }


            Column(Modifier.weight(1f)) {
                Text(catalogueItem.name, fontWeight = FontWeight.Bold, maxLines = 2, overflow = TextOverflow.Ellipsis)
                Surface(
                    shape = MaterialTheme.shapes.small,
                    border = BorderStroke(1.dp, Color.LightGray),
                    color = Color.Transparent,
                ) {
                    Text(
                        text = catalogueItem.category.uppercase(),
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.Gray,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
                Text("₱${"%.2f".format(catalogueItem.price)}", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold, color = Color.Gray)
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.width(120.dp)
            ) {
                QuantityStepper(
                    initialValue = initialQuantity,
                    minValue = 0,
                    maxValue = stockLeft,
                    onValueChange = onQuantityChange
                )
                Text("$stockLeft in Stock", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
            }
        }
    }
}

@Composable
private fun TransactionTotalRow(totalAmount: Double) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Total",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "₱${"%.2f".format(totalAmount)}",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = AlleyMainOrange
        )
    }
}