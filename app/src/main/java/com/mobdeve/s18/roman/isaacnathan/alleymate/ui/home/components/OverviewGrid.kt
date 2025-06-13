package com.mobdeve.s18.roman.isaacnathan.alleymate.ui.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun OverviewGrid() {
    // ─── Main Grid Layout ─────────────────────────────────────
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        // ── First Row ──
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OverviewStatCard(
                value = "₱4500",
                label = "Last Event Sales",
                modifier = Modifier.weight(1f)
            )
            OverviewStatCard(
                value = "₱7525.25",
                label = "Average Profit per Event",
                modifier = Modifier.weight(1f)
            )
        }

        // ── Second Row ──
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OverviewStatCard(
                value = "10 Items",
                label = "Products in Catalogue",
                modifier = Modifier.weight(1f)
            )
            OverviewStatCard(
                value = "5 Items",
                label = "Low in Stock",
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Preview
@Composable
private fun OverviewGridPreview() {
    OverviewGrid()
}
