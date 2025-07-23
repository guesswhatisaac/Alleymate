package com.mobdeve.s18.roman.isaacnathan.alleymate.ui.allocate.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mobdeve.s18.roman.isaacnathan.alleymate.common.components.AppCard
import com.mobdeve.s18.roman.isaacnathan.alleymate.common.components.modal.QuantityStepper
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.CatalogueItem
import androidx.compose.ui.layout.ContentScale
import androidx.core.net.toUri
import coil.compose.AsyncImage

@Composable
fun AllocationItem(
    item: CatalogueItem,
    onQuantityChange: (Int) -> Unit,
    onRemoveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    AppCard(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // Remove item button
            IconButton(onClick = onRemoveClick) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Remove Item",
                    tint = MaterialTheme.colorScheme.error
                )
            }

            // Item image or placeholder
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .background(Color(0xFFECE6F0), shape = MaterialTheme.shapes.medium),
                contentAlignment = Alignment.Center,
            ) {
                if (item.imageUri.isNullOrBlank()) {
                    Icon(
                        imageVector = Icons.Outlined.Image,
                        contentDescription = "Image Placeholder",
                        modifier = Modifier.size(64.dp),
                        tint = Color(0xFFC9C3CD),
                    )
                } else {
                    AsyncImage(
                        model = item.imageUri.toUri(),
                        contentDescription = "${item.name} image",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(MaterialTheme.shapes.medium),
                        contentScale = ContentScale.Crop,
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Item details: name, category, and price
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
                Surface(
                    shape = MaterialTheme.shapes.small,
                    border = BorderStroke(1.dp, Color.LightGray),
                    color = Color.Transparent,
                ) {
                    Text(
                        text = item.category.uppercase(),
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.Gray,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
                Text(
                    text = "₱${item.price}",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Quantity selector and stock display
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.width(120.dp)
            ) {
                QuantityStepper(
                    onValueChange = onQuantityChange,
                    maxValue = item.stock,
                    minValue = 0
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${item.stock} in Stock",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray
                )
            }
        }
    }
}
