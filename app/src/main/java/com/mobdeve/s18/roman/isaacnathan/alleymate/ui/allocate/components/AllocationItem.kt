package com.mobdeve.s18.roman.isaacnathan.alleymate.ui.allocate.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mobdeve.s18.roman.isaacnathan.alleymate.common.components.AppCard
import com.mobdeve.s18.roman.isaacnathan.alleymate.common.components.modal.QuantityStepper
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.CatalogueItem

@Composable
fun AllocationItem(
    item: CatalogueItem,
    onQuantityChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    AppCard(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // --- Image Placeholder ---
            Box(
                modifier = Modifier
                    .size(72.dp) // Slightly smaller for a more compact look
                    .background(Color(0xFFECE6F0), shape = MaterialTheme.shapes.medium),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Image,
                    contentDescription = "Item Image",
                    modifier = Modifier.size(36.dp),
                    tint = Color(0xFFC9C3CD)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // --- Details Column ---
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

            // --- Quantity Stepper Column ---
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.width(120.dp)
            ) {
                QuantityStepper(onValueChange = onQuantityChange)
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
