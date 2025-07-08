package com.mobdeve.s18.roman.isaacnathan.alleymate.ui.live_sale.components

// ... imports from your existing files ...
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.Event

@Composable
fun LiveSaleInfoBar(event: Event, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        shape = RoundedCornerShape(0.dp) // Make it a flat bar
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            DetailRow(label = "Date", value = event.dateRangeString)
            DetailRow(label = "Location", value = event.location)
            DetailRow(label = "Status", value = event.status.name)
        }
    }
}

@Composable
fun TransactionStatsHeader(event: Event, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            CompactStatCard(
                modifier = Modifier.weight(1f),
                value = "${event.totalItemsSold}",
                label = "Items Sold"
            )
            CompactStatCard(
                modifier = Modifier.weight(1f),
                value = "₱${"%.2f".format(event.totalRevenueInCents / 100.0)}",
                label = "Revenue"
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            CompactStatCard(
                modifier = Modifier.weight(1f),
                value = "₱${"%.2f".format(event.totalExpensesInCents / 100.0)}",
                label = "Expenses"
            )
            CompactStatCard(
                modifier = Modifier.weight(1f),
                value = "₱${"%.2f".format(event.profitInCents / 100.0)}",
                label = "Profit"
            )
        }
    }
}

@Composable
fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun CompactStatCard(
    modifier: Modifier = Modifier,
    value: String,
    label: String
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray
            )
        }
    }
}