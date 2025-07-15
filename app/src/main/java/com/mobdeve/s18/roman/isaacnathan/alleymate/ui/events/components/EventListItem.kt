package com.mobdeve.s18.roman.isaacnathan.alleymate.ui.events.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.EventStatus
import com.mobdeve.s18.roman.isaacnathan.alleymate.ui.events.EventUiModel

@Composable
fun EventListItem(
    event: EventUiModel,
    onEventClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onEventClick() },
        shape = MaterialTheme.shapes.medium,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // --- HEADER: Title ---
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = event.title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }

            // --- BODY: Details and Stats ---
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // Top Section: Date and Location
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = event.dateRangeString,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = event.location,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Bottom Section: Status Tag (left) and Stats (right)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Status Tag - Bottom Left
                    StatusTag(status = event.status)

                    // Stats - Bottom Right
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(24.dp),
                        verticalAlignment = Alignment.Bottom
                    ) {
                        StatColumn(
                            value = event.catalogueCount.toString(),
                            label = "Catalogue"
                        )
                        StatColumn(
                            value = event.totalStockLeft.toString(),
                            label = "Stock Left"
                        )
                    }
                }
            }
        }
    }
}

// --- Helper Composables ---
@Composable
private fun StatColumn(
    value: String,
    label: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.End
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = Color.Gray
        )
    }
}

@Composable
private fun StatusTag(status: EventStatus) {
    val tagBackgroundColor = when (status) {
        EventStatus.LIVE -> Color(0xFF4CAF50).copy(alpha = 0.15f)
        EventStatus.UPCOMING -> MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
        EventStatus.ENDED -> Color.Gray.copy(alpha = 0.15f)
    }

    val tagTextColor = when (status) {
        EventStatus.LIVE -> Color(0xFF388E3C)
        EventStatus.UPCOMING -> MaterialTheme.colorScheme.primary
        EventStatus.ENDED -> Color.Gray
    }

    Surface(
        shape = MaterialTheme.shapes.small,
        border = BorderStroke(1.dp, color = tagTextColor.copy(alpha = 0.5f)),
        color = tagBackgroundColor
    ) {
        Text(
            text = status.name,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
            style = MaterialTheme.typography.labelSmall,
            color = tagTextColor,
            fontWeight = FontWeight.Bold
        )
    }
}
