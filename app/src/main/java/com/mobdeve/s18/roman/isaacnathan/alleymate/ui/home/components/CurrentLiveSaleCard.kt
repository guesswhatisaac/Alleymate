package com.mobdeve.s18.roman.isaacnathan.alleymate.ui.home.components

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

@Composable
fun CurrentLiveSaleCard() {
    val buttonColor = Color(0xFFEF6C42)

    AppCard(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // Left Side
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.Start
            ) {
                // REFACTORED: Use titleLarge for the event title
                Text(
                    text = "Paskomiket '24",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.Black
                )
                // REFACTORED: Use bodyMedium for the date
                Text(
                    text = "December 24 - December 26",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = { /* TODO: Handle continue click */ },
                    shape = MaterialTheme.shapes.extraLarge,
                    colors = ButtonDefaults.buttonColors(containerColor = buttonColor)
                ) {
                    Icon(Icons.Default.PlayArrow, contentDescription = "Continue", modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    // REFACTORED: Use bodyMedium for the button text
                    Text(
                        text = "Continue",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold // Override to bold
                    )
                }
            }

            VerticalDivider(
                modifier = Modifier.height(80.dp).padding(horizontal = 8.dp),
                color = Color.LightGray.copy(alpha = 0.5f)
            )

            // Right Side
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                StatColumn(value = "₱450", label = "Revenue")
                StatColumn(value = "214x", label = "Sold")
                StatColumn(value = "102x", label = "Trans.")
            }
        }
    }
}

@Composable
private fun StatColumn(value: String, label: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // REFACTORED: Use bodyMedium for the stat value, overriding to bold
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Black,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(2.dp))
        // REFACTORED: Use labelSmall for the stat label
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = Color.Gray
        )
    }
}

@Preview
@Composable
private fun CurrentLiveSaleCardPreview() {
    CurrentLiveSaleCard()
}