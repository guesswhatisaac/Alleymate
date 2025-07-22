package com.mobdeve.s18.roman.isaacnathan.alleymate.ui.reports.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mobdeve.s18.roman.isaacnathan.alleymate.theme.AlleyMainOrange
import com.mobdeve.s18.roman.isaacnathan.alleymate.ui.reports.ReportStats

@Composable
fun StatsGrid(
    stats: ReportStats,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // First Row
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            StatCard(
                label = "Total Revenue",
                value = "₱${"%.2f".format(stats.totalRevenueInCents / 100.0)}",
                modifier = Modifier.weight(1f)
            )
            StatCard(
                label = "Total Expenses",
                value = "₱${"%.2f".format(stats.totalExpensesInCents / 100.0)}",
                modifier = Modifier.weight(1f)
            )
        }
        // Second Row
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            StatCard(
                label = "Net Profit",
                value = "₱${"%.2f".format(stats.netProfitInCents / 100.0)}",
                modifier = Modifier.weight(1f)
            )
            StatCard(
                label = "Items Sold",
                value = stats.itemsSoldCount.toString(),
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun StatCard(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = AlleyMainOrange
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = Color.Gray
        )
    }
}