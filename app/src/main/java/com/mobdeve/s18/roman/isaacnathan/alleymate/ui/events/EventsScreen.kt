package com.mobdeve.s18.roman.isaacnathan.alleymate.ui.events

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.mobdeve.s18.roman.isaacnathan.alleymate.common.components.AppTopBar

@Composable
fun EventsScreen() {
    Scaffold(
        topBar = {
            AppTopBar(title = "Events")
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Events Screen")
        }
    }
}