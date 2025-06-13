package com.mobdeve.s18.roman.isaacnathan.alleymate.common.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun AppFloatingActionButton(

    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    FloatingActionButton(

        modifier = modifier.padding(bottom = 16.dp, end = 8.dp),
        onClick = onClick,
        shape = MaterialTheme.shapes.extraLarge,
        containerColor = Color(0xFFEF6C42),
        contentColor = MaterialTheme.colorScheme.onPrimary,
        elevation = FloatingActionButtonDefaults.elevation(
            defaultElevation = 2.dp,
            pressedElevation = 4.dp
        )
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Add"
        )
    }
}

@Preview
@Composable
private fun AppFloatingActionButtonPreview() {
    AppFloatingActionButton {}
}