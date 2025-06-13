package com.mobdeve.s18.roman.isaacnathan.alleymate.common.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

/**
 * A custom, themed wrapper around the Material Card.
 * This ensures all cards in the app share a consistent look and feel.
 *
 * @param modifier The modifier to be applied to the card.
 * @param shape The corner shape of the card. Defaults to the theme's medium shape.
 * @param hasBorder If true, a subtle border will be drawn. Defaults to true.
 * @param content The content to be placed inside the card.
 */
@Composable
fun AppCard(
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.medium,
    hasBorder: Boolean = true,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier,
        shape = shape,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        border = if (hasBorder) {
            BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
        } else {
            null
        }
    ) {
        content()
    }
}