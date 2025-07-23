package com.mobdeve.s18.roman.isaacnathan.alleymate.ui.live_sale.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import coil.compose.AsyncImage
import com.mobdeve.s18.roman.isaacnathan.alleymate.common.components.AppCard
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.relations.EventInventoryWithDetails
import com.mobdeve.s18.roman.isaacnathan.alleymate.theme.AlleyMainOrange

@Composable
fun SelectableItemCard(
    inventoryItem: EventInventoryWithDetails,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Compute remaining stock
    val stockLeft = inventoryItem.eventInventoryItem.allocatedQuantity - inventoryItem.eventInventoryItem.soldQuantity
    val isEnabled = stockLeft > 0

    AppCard(
        modifier = modifier.clickable(enabled = isEnabled, onClick = onClick),
    ) {
        // Dim card background if out of stock
        Column(modifier = Modifier.background(if (!isEnabled) Color.LightGray.copy(alpha = 0.3f) else Color.Transparent)) {

            Box(
                modifier = Modifier.fillMaxWidth().height(120.dp),
                contentAlignment = Alignment.Center
            ) {
                // Show placeholder if no image, else load image
                if (inventoryItem.catalogueItem.imageUri.isNullOrBlank()) {
                    Icon(
                        imageVector = Icons.Outlined.Image,
                        contentDescription = "Image Placeholder",
                        modifier = Modifier.size(64.dp),
                        tint = Color(0xFFC9C3CD)
                    )
                } else {
                    AsyncImage(
                        model = inventoryItem.catalogueItem.imageUri.toUri(),
                        contentDescription = "${inventoryItem.catalogueItem.name} image",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }

                // Overlay selection indicator
                if (isSelected) {
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .background(AlleyMainOrange.copy(alpha = 0.3f))
                    )
                    Icon(
                        imageVector = Icons.Filled.CheckCircle,
                        contentDescription = "Selected",
                        tint = Color.White,
                        modifier = Modifier.align(Alignment.Center).size(40.dp)
                    )
                }
            }

            // Item name, price, and stock count
            Column(
                modifier = Modifier.padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = inventoryItem.catalogueItem.name,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "₱${"%.2f".format(inventoryItem.catalogueItem.price)}",
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.Gray
                )
                Text(
                    text = "$stockLeft in Stock",
                    style = MaterialTheme.typography.labelSmall,
                    color = if (isEnabled) Color.Gray else MaterialTheme.colorScheme.error,
                    fontWeight = if (isEnabled) FontWeight.Normal else FontWeight.Bold
                )
            }
        }
    }
}
