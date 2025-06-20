package com.mobdeve.s18.roman.isaacnathan.alleymate.ui.events.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mobdeve.s18.roman.isaacnathan.alleymate.common.components.modal.BaseModal
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.CatalogueItem
import com.mobdeve.s18.roman.isaacnathan.alleymate.theme.AlleyMateTheme

@Composable
fun EventInventoryModal(
    eventName: String,
    inventory: List<CatalogueItem>,
    onDismissRequest: () -> Unit,
    onRemoveItem: (CatalogueItem) -> Unit
) {
    BaseModal(
        onDismissRequest = onDismissRequest,
        headerTitle = "$eventName Inventory"
    ) {
        if (inventory.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxWidth().padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("No items allocated to this event.", color = Color.Gray)
            }
        } else {
            LazyColumn(
                modifier = Modifier.heightIn(max = 400.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(inventory) { item ->
                    InventoryItemRow(
                        item = item,
                        onRemoveClick = { onRemoveItem(item) }
                    )
                }
            }
        }
    }
}

@Composable
private fun InventoryItemRow(
    item: CatalogueItem,
    onRemoveClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(item.name, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
            Text(
                "Allocated: ${item.stock}",
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray
            )
        }
        IconButton(onClick = onRemoveClick) {
            Icon(Icons.Default.Delete, contentDescription = "Remove Item", tint = MaterialTheme.colorScheme.error)
        }
    }
}

@Preview
@Composable
private fun EventInventoryModalPreview() {
    AlleyMateTheme {
        EventInventoryModal(
            eventName = "KOMIKET '25",
            inventory = listOf(
                CatalogueItem(1, "MHYLOW star sticker", "Sticker", 100, 50),
                CatalogueItem(2, "Art Print A", "Print", 250, 20)
            ),
            onDismissRequest = {},
            onRemoveItem = {}
        )
    }
}