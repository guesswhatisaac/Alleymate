package com.mobdeve.s18.roman.isaacnathan.alleymate.ui.catalogue.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mobdeve.s18.roman.isaacnathan.alleymate.common.components.modal.BaseModal
import com.mobdeve.s18.roman.isaacnathan.alleymate.common.components.modal.QuantityStepper
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.CatalogueItem
import com.mobdeve.s18.roman.isaacnathan.alleymate.theme.AlleyMainOrange

@Composable
fun RestockProductModal(
    item: CatalogueItem,
    onDismissRequest: () -> Unit,
    onConfirmRestock: (Int) -> Unit
) {
    BaseModal(
        onDismissRequest = onDismissRequest,
        headerTitle = "Restock Product"
    ) {
        var restockQuantity by remember { mutableIntStateOf(0) }

        Row(
            modifier = Modifier.padding(vertical = 16.dp).height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // --- Left Side: Product Info ---
            Column(
                modifier = Modifier.weight(1f).fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Top content group
                Column {
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

                // Bottom content (pushed to the bottom by Arrangement.SpaceBetween)
                Text(
                    text = "${item.stock} in Stock",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray
                )
            }

            // Vertical Divider
            VerticalDivider(modifier = Modifier.fillMaxHeight().padding(horizontal = 8.dp))

            // --- Right Side: Actions ---
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Restock Quantity",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray
                )
                QuantityStepper(
                    initialValue = restockQuantity,
                    onValueChange = { restockQuantity = it },
                )
                Button(
                    onClick = { onConfirmRestock(restockQuantity) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium,
                    colors = ButtonDefaults.buttonColors(containerColor = AlleyMainOrange),
                    enabled = restockQuantity > 0
                ) {
                    Text("RESTOCK")
                }
            }
        }
    }
}


