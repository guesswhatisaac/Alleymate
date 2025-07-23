package com.mobdeve.s18.roman.isaacnathan.alleymate.ui.reports.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mobdeve.s18.roman.isaacnathan.alleymate.common.components.AppCard
import com.mobdeve.s18.roman.isaacnathan.alleymate.ui.reports.BestSeller

@Composable
fun BestSellerListItem(
    bestSeller: BestSeller,
    modifier: Modifier = Modifier
) {
    // Wrapper card for each best seller item
    AppCard(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Display rank number (e.g., #1)
            Text(
                text = "#${bestSeller.rank}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            // Item name with flexible width to avoid overflow
            Text(
                text = bestSeller.name,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f),
                fontWeight = FontWeight.Medium
            )

            // Show sales statistics (quantity + total revenue)
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "${bestSeller.quantitySold} sold",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "₱${"%.2f".format(bestSeller.totalRevenueInCents / 100.0)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
        }
    }
}
