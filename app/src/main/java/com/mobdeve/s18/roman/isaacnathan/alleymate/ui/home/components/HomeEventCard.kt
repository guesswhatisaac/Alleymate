package com.mobdeve.s18.roman.isaacnathan.alleymate.ui.home.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.Event

@Composable
fun EventCard(event: Event) {
    // ─── Card Container ─────────────────────────────────────
    Card(
        modifier = Modifier
            .width(280.dp)
            .height(140.dp),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {

        // ─── Card Content ─────────────────────────────────────
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            // Title
            Text(
                text = event.title,
                style = MaterialTheme.typography.titleLarge,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Date
            Text(
                text = event.date,
                style = MaterialTheme.typography.bodyMedium
            )

            // Location
            Text(
                text = event.location,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
