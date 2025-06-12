package com.mobdeve.s18.roman.isaacnathan.alleymate.ui.catalogue

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
fun CatalogueScreen() {
    Scaffold(
        topBar = {
            AppTopBar(title = "Catalogue")
        }
    ) { innerPadding ->
        // The Box now uses the padding from the Scaffold
        // to avoid being drawn underneath the TopBar.
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Catalogue Screen")
        }
    }
}