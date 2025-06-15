package com.mobdeve.s18.roman.isaacnathan.alleymate.common.components.modal

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun FormImagePicker(
    modifier: Modifier = Modifier,
    onImagePickerClick: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(120.dp)
            .clip(MaterialTheme.shapes.medium)
            .background(Color.LightGray)
            .border(
                BorderStroke(width = 2.dp, color = MaterialTheme.colorScheme.outlineVariant),
                shape = MaterialTheme.shapes.medium
            )
            .clickable { onImagePickerClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.AddAPhoto,
            contentDescription = "Add Photo",
            tint = Color.White,
            modifier = Modifier.size(48.dp)
        )
    }
}