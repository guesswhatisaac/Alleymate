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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mobdeve.s18.roman.isaacnathan.alleymate.common.components.AppCard
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.Transaction
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.TransactionItem
import com.mobdeve.s18.roman.isaacnathan.alleymate.theme.AlleyMateTheme

@Composable
fun TransactionListItem(
    transaction: Transaction,
    modifier: Modifier = Modifier
) {
    AppCard(modifier = modifier) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(horizontalAlignment = Alignment.Start) {
                    Text(
                        text = "${transaction.totalQuantity} items",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = transaction.time,
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.Gray
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "₱${transaction.totalPrice}",
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

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                transaction.items.forEach { item ->
                    TransactionItemRow(item = item)
                }
            }
        }
    }
}

@Composable
private fun TransactionItemRow(item: TransactionItem) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = item.name, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
            Surface(
                shape = MaterialTheme.shapes.extraSmall,
                border = BorderStroke(1.dp, Color.LightGray),
                color = Color.Transparent,
                modifier = Modifier.padding(top = 4.dp)
            ) {
                Text(
                    text = item.category.uppercase(),
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 8.sp),
                    color = Color.Gray,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }
        }

        Text(
            text = "₱${item.price} x ${item.quantity}",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            textAlign = TextAlign.End
        )
    }
}


@Preview
@Composable
private fun TransactionListItemPreview() {
    val sampleTransaction = Transaction(
        id = 1,
        time = "10:00AM",
        items = listOf(
            TransactionItem(1, "A Very Long MHYLOW Star Sticker Name", "Sticker", 100, 3),
            TransactionItem(2, "MHYLOW star sticker", "Sticker", 30, 1),
            TransactionItem(3, "Another Item", "Print", 250, 5)
        )
    )
    AlleyMateTheme {
        TransactionListItem(transaction = sampleTransaction)
    }
}