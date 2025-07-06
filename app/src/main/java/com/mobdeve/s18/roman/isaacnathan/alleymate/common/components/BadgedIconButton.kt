package com.mobdeve.s18.roman.isaacnathan.alleymate.common.components

import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun BadgedIconButton(
    badgeCount: Int,
    icon: ImageVector,
    contentDescription: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    BadgedBox(
        modifier = modifier.offset(x = (-8).dp),
        badge = {
            if (badgeCount > 0) {
                Badge(
                    modifier = Modifier.offset(x = (-6).dp, y = 6.dp),
                    containerColor = Color(0xFFEF6C42)
                ) {
                    Text(
                        text = badgeCount.toString(),
                        color = Color.White
                    )
                }
            }
        }
    ) {
        IconButton(onClick = onClick) {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription,
                modifier = Modifier.size(32.dp),
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}
