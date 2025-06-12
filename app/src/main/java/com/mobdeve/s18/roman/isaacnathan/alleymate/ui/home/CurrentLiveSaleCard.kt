
package com.mobdeve.s18.roman.isaacnathan.alleymate.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.sp

@Composable
fun CurrentLiveSaleCard() {
    val cardBackgroundColor = Color(0xFFFAF7FF)
    val buttonColor = Color(0xFFEF6C42)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = cardBackgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
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
                Text(
                    text = "Paskomiket '24",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Color.Black
                )
                Text(
                    text = "December 24 - December 26",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = { /* TODO: Handle continue click */ },
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(containerColor = buttonColor)
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Continue",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Continue")
                }
            }

            // Vertical Divider
            VerticalDivider(
                modifier = Modifier
                    .height(80.dp)
                    .padding(horizontal = 8.dp),
                color = Color.LightGray.copy(alpha = 0.5f)
            )

            // Right Side
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                StatRow(label = "Gross Revenue", value = "₱450")
                StatRow(label = "Items Sold", value = "214x")
                StatRow(label = "Transactions", value = "102x")
            }
        }
    }
}

@Composable
private fun StatRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, fontSize = 14.sp, color = Color.Gray)
        Text(text = value, fontSize = 14.sp, color = Color.Black, fontWeight = FontWeight.SemiBold)
    }
}

@Preview
@Composable
private fun CurrentLiveSaleCardPreview() {
    CurrentLiveSaleCard()
}