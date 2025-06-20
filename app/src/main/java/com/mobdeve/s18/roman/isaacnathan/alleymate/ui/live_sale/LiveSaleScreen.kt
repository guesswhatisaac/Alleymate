package com.mobdeve.s18.roman.isaacnathan.alleymate.ui.live_sale

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mobdeve.s18.roman.isaacnathan.alleymate.common.components.InfoBar
import com.mobdeve.s18.roman.isaacnathan.alleymate.common.components.LiveSaleTopBar
import com.mobdeve.s18.roman.isaacnathan.alleymate.common.components.SectionHeader
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.Transaction
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.TransactionItem
import com.mobdeve.s18.roman.isaacnathan.alleymate.theme.AlleyMainOrange
import com.mobdeve.s18.roman.isaacnathan.alleymate.theme.AlleyMateTheme
import com.mobdeve.s18.roman.isaacnathan.alleymate.ui.live_sale.components.TransactionListItem

@Composable
fun LiveSaleScreen(onNavigateBack: () -> Unit) {
    Scaffold(
        topBar = {
            LiveSaleTopBar(
                title = "Live Sale",
                onNavigateBack = onNavigateBack
            )
        }
    ) { innerPadding ->
        val transactions = listOf(
            Transaction(1, "10:00AM", listOf(TransactionItem(1, "MHYLOW star sticker", "Sticker", 100, 3), TransactionItem(2, "MHYLOW star sticker", "Sticker", 30, 1))),
            Transaction(2, "10:05AM", listOf(TransactionItem(3, "Art Print A", "Print", 250, 1))),
            Transaction(3, "10:12AM", listOf(TransactionItem(1, "MHYLOW star sticker", "Sticker", 100, 1)))
        )

        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 100.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                item {
                    InfoBar(
                        title = "KOMIKET '25",
                        subtitle = "October 25 - October 27"
                    )
                }

                // Stats Chips
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        StatChip(label = "Gross Revenue", value = "₱4500", modifier = Modifier.weight(1f))
                        StatChip(label = "Gross Revenue", value = "₱4500", modifier = Modifier.weight(1f))
                    }
                }

                // Transactions Header
                item {
                    SectionHeader(
                        title = "Transactions",
                        showDivider = true,
                        isSubtle = false,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }

                // Transaction List
                items(transactions) { transaction ->
                    TransactionListItem(
                        transaction = transaction,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }

            // Floating "Add" Button
            Button(
                onClick = { /* TODO: Open add transaction modal */ },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
                    .fillMaxWidth(0.6f) // Take 60% of the width
                    .height(64.dp),
                shape = MaterialTheme.shapes.medium,
                colors = ButtonDefaults.buttonColors(containerColor = AlleyMainOrange)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Transaction", modifier = Modifier.size(32.dp))
            }
        }
    }
}


// --- 2. POLISHED the StatChip ---
@Composable
private fun StatChip(label: String, value: String, modifier: Modifier = Modifier) {
    OutlinedCard(modifier = modifier) {
        Column(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(label, style = MaterialTheme.typography.labelSmall)
            Text(value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LiveSaleScreenPreview() {
    AlleyMateTheme {
        LiveSaleScreen(onNavigateBack = {})
    }
}