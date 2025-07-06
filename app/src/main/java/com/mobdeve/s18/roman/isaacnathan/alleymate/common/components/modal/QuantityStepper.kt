package com.mobdeve.s18.roman.isaacnathan.alleymate.common.components.modal

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun QuantityStepper(
    initialValue: Int = 0,
    onValueChange: (Int) -> Unit,
    maxValue: Int = Int.MAX_VALUE
) {
    var count by remember { mutableIntStateOf(initialValue) }

    val shape = MaterialTheme.shapes.extraLarge

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .height(48.dp)
            .border(BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant), shape = shape)
    ) {
        // Minus Button
        IconButton(
            onClick = {
                if (count > 0) {
                    count--
                    onValueChange(count)
                }
            },
            modifier = Modifier.weight(1f)
        ) {
            Icon(Icons.Default.Remove, contentDescription = "Decrease quantity")
        }

        // Divider
        VerticalDivider(modifier = Modifier.fillMaxHeight())

        // Count Text
        Text(
            text = count.toString(),
            modifier = Modifier.weight(1.5f),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge
        )

        // Divider
        VerticalDivider(modifier = Modifier.fillMaxHeight())

        // Plus Button
        IconButton(
            onClick = { if (count < maxValue) count++ ; onValueChange(count) },
            enabled = count < maxValue,
            modifier = Modifier.weight(1f)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Increase quantity")
        }
    }
}
