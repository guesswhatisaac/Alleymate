package com.mobdeve.s18.roman.isaacnathan.alleymate.ui.home.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mobdeve.s18.roman.isaacnathan.alleymate.common.components.AppCard
import com.mobdeve.s18.roman.isaacnathan.alleymate.common.components.StatColumn

@Composable
fun CurrentLiveSaleCard(
    onContinueClick: () -> Unit
) {
    val buttonColor = Color(0xFFEF6C42)

    AppCard(
        modifier = Modifier.fillMaxWidth(),
        content = {
            // Main layout column
            Column(
                modifier = Modifier.padding(16.dp)
            ) {

                // ─── TOP SECTION ─────────────────────────────────────
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    // Title and date
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Paskomiket '24",
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.Black
                        )
                        Text(
                            text = "December 24 - December 26",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // Continue button
                    Button(
                        onClick = onContinueClick,
                        shape = MaterialTheme.shapes.extraLarge,
                        colors = ButtonDefaults.buttonColors(containerColor = buttonColor)
                    ) {
                        Icon(
                            Icons.Default.PlayArrow,
                            contentDescription = "Continue",
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Continue",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // ─── DIVIDER ─────────────────────────────────────────
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 16.dp),
                    color = Color.LightGray.copy(alpha = 0.5f)
                )

                // ─── BOTTOM STATS SECTION ───────────────────────────
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    StatColumn(value = "₱450", label = "Revenue")
                    StatColumn(value = "214x", label = "Sold")
                    StatColumn(value = "102x", label = "Transactions")
                }
            }
        },
    )
}

@Preview
@Composable
private fun CurrentLiveSaleCardPreview() {
    CurrentLiveSaleCard(onContinueClick = {})
}
