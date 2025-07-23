package com.mobdeve.s18.roman.isaacnathan.alleymate.ui.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.mobdeve.s18.roman.isaacnathan.alleymate.ui.home.HomeOverviewStats

@Composable
fun OverviewGrid(
    stats: HomeOverviewStats,
    modifier: Modifier = Modifier
) {
    // Arranges stats into a 2x2 grid using two rows
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Row 1: revenue + total catalogue items
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            OverviewStatCard(
                label = "Last 30 Days Revenue",
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "₱${"%.2f".format(stats.last30DaysRevenueInCents / 100.0)}",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            }

            OverviewStatCard(
                label = "Total Items in Catalogue",
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = stats.catalogueItemsCount.toString(),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Row 2: best seller name + count of low stock items
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            OverviewStatCard(
                label = "Best Seller (All Time)",
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = stats.bestSellerName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            OverviewStatCard(
                label = "Low Stock Items",
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = stats.lowStockItemsCount.toString(),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
