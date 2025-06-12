package com.mobdeve.s18.roman.isaacnathan.alleymate.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun EventCard(event: Event) {
    Card(
        modifier = Modifier
            .width(280.dp) // Set a fixed width for the card
            .height(140.dp),
        shape = RoundedCornerShape(20.dp), // Nicely rounded corners
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp) // Gives that nice shadow
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = event.title,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = event.date,
                fontSize = 16.sp,
                color = Color.DarkGray
            )
            Text(
                text = event.location,
                fontSize = 16.sp,
                color = Color.DarkGray
            )
        }
    }
}