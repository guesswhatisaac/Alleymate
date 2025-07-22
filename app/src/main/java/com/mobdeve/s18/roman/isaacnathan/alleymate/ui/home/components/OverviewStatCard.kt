package com.mobdeve.s18.roman.isaacnathan.alleymate.ui.home.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mobdeve.s18.roman.isaacnathan.alleymate.common.components.AppCard

@Composable
fun OverviewStatCard(
    label: String,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    // UPDATED: Use a fixed height for a cleaner grid alignment
    AppCard(
        modifier = modifier.height(100.dp),
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                // UPDATED: Use SpaceEvenly for better vertical balance
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                // The main content (e.g., the number or item name)
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    this@Column.content()
                }

                // The label at the bottom
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelMedium, // Slightly larger label
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                    maxLines = 2 // Allow label to wrap if needed
                )
            }
        }
    )
}