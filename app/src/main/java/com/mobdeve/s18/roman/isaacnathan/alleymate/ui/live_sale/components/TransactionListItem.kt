package com.mobdeve.s18.roman.isaacnathan.alleymate.ui.live_sale.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mobdeve.s18.roman.isaacnathan.alleymate.common.components.AppCard
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.relations.TransactionItemWithDetails
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.relations.TransactionWithItems
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun TransactionListItem(
    transactionWithItems: TransactionWithItems,
    modifier: Modifier = Modifier
) {
    val transaction = transactionWithItems.transaction
    val itemsWithDetails = transactionWithItems.items

    // Compute total quantity and total cost from all items in the transaction
    val totalQuantity = itemsWithDetails.sumOf { it.saleTransactionItem.quantity }
    val totalPriceInCents = itemsWithDetails.sumOf {
        it.saleTransactionItem.quantity * it.saleTransactionItem.priceInCents
    }

    // Format transaction timestamp to display time only
    val timestamp = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date(transaction.timestamp))

    AppCard(modifier = modifier) {
        Column {
            // Header row: quantity and time on the left, price on the right
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(horizontalAlignment = Alignment.Start) {
                    Text(
                        text = "$totalQuantity items",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = timestamp,
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.Gray
                    )
                }

                Spacer(modifier = Modifier.weight(1f)) // Push price column to the end

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "₱${"%.2f".format(totalPriceInCents / 100.0)}",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Total",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.Gray
                    )
                }
            }

            // Divider between header and item list
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

            // Item details
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Display each item in the transaction
                itemsWithDetails.forEach { itemDetail ->
                    TransactionItemRow(item = itemDetail)
                }
            }
        }
    }
}

@Composable
private fun TransactionItemRow(
    item: TransactionItemWithDetails
) {
    val catalogueItem = item.catalogueItem
    val saleItem = item.saleTransactionItem

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            // Show item name
            Text(
                text = catalogueItem.name,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold
            )

            // Display category tag with border
            Surface(
                shape = MaterialTheme.shapes.extraSmall,
                border = BorderStroke(1.dp, Color.LightGray),
                color = Color.Transparent,
                modifier = Modifier.padding(top = 4.dp)
            ) {
                Text(
                    text = catalogueItem.category.uppercase(),
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 8.sp),
                    color = Color.Gray,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }
        }

        // Show item price and quantity on the right
        Text(
            text = "₱${"%.2f".format(saleItem.priceInCents / 100.0)} x ${saleItem.quantity}",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            textAlign = TextAlign.End
        )
    }
}
