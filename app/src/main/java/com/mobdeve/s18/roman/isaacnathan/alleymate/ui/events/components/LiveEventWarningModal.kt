package com.mobdeve.s18.roman.isaacnathan.alleymate.ui.events.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mobdeve.s18.roman.isaacnathan.alleymate.common.components.modal.BaseModal
import com.mobdeve.s18.roman.isaacnathan.alleymate.theme.AlleyMainOrange
@Composable
fun LiveEventWarningModal(
    existingLiveEventTitle: String,
    onDismissRequest: () -> Unit
) {
    BaseModal(
        onDismissRequest = onDismissRequest,
        headerTitle = "Live Event in Progress"
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Initial confirmation question
            Text(
                text = "Cannot start a new live sale.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )

            // Event details card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                shape = MaterialTheme.shapes.medium,
                border = CardDefaults.outlinedCardBorder()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = existingLiveEventTitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Black,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "This event is currently live.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Black,
                        textAlign = TextAlign.Center
                    )
                }
            }

            // Warning message
            Text(
                text = "Please end the current sale before starting a new one.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFFF44336), // same red as DeleteEventModal
                textAlign = TextAlign.Center
            )

            // Dismiss button
            Button(
                onClick = onDismissRequest,
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFF44336) // same red for consistency
                )
            ) {
                Text(
                    text = "OK",
                    modifier = Modifier.padding(vertical = 8.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}