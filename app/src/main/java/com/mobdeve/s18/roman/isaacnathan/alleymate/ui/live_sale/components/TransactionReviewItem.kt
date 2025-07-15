package com.mobdeve.s18.roman.isaacnathan.alleymate.ui.live_sale.components

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
fun TransactionReviewItem(
    item: CatalogueItem,
    stockLeft: Int,
    onQuantityChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    AppCard(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp, horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.Image,
                contentDescription = "Item Image",
                modifier = Modifier.size(64.dp),
                tint = Color(0xFFC9C3CD)
            )
            Column(Modifier.weight(1f)) {
                Text(text = item.name, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                Text(text = "₱${"%.2f".format(item.price)}", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                QuantityStepper(
                    maxValue = stockLeft,
                    onValueChange = onQuantityChange,
                    minValue = 0
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "$stockLeft in Stock",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray
                )
            }
        }
    }
}