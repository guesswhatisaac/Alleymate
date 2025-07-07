package com.mobdeve.s18.roman.isaacnathan.alleymate.ui.catalogue.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import androidx.core.net.toUri
import coil.compose.AsyncImage
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.CatalogueItem
import com.mobdeve.s18.roman.isaacnathan.alleymate.common.components.AppCard

@Composable
fun CatalogueItemCard(
    item: CatalogueItem,
    isSelected: Boolean,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    onRestockClick: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    AppCard(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick
            ),
        content = {
            Column {
                // --- Image Placeholder with Action Button ---
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .background(Color(0xFFECE6F0)),
                    contentAlignment = Alignment.Center,
                ) {
                    if (item.imageUri.isNullOrBlank()) {
                        Icon(
                            imageVector = Icons.Outlined.Image,
                            contentDescription = "Image Placeholder",
                            modifier = Modifier.size(64.dp),
                            tint = Color(0xFFC9C3CD)
                        )
                    } else {
                        AsyncImage(
                            model = item.imageUri.toUri(),
                            contentDescription = "${item.name} image",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                    if (isSelected) {
                        Box(
                            modifier = Modifier
                                .matchParentSize()
                                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.3f))
                        )
                        Icon(
                            imageVector = Icons.Filled.CheckCircle,
                            contentDescription = "Selected",
                            tint = Color.White,
                            modifier = Modifier.align(Alignment.Center).size(48.dp)
                        )
                    }
                    // --- Dropdown Menu for all actions ---
                    if (!isSelected) {
                        Box(modifier = Modifier.align(Alignment.TopEnd)) {
                            var menuExpanded by remember { mutableStateOf(false) }
                            IconButton(onClick = { menuExpanded = true }) {
                                Icon(
                                    Icons.Default.MoreVert,
                                    contentDescription = "More options",
                                    tint = Color.Gray
                                )
                            }
                            DropdownMenu(
                                expanded = menuExpanded,
                                onDismissRequest = { menuExpanded = false },
                                modifier = Modifier
                                    .width(120.dp)
                                    .background(MaterialTheme.colorScheme.surface),
                                properties = PopupProperties(focusable = true)
                            ) {
                                DropdownMenuItem(
                                    text = { Text("Edit") },
                                    onClick = {
                                        onEditClick()
                                        menuExpanded = false
                                    },
                                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                                )
                                DropdownMenuItem(
                                    text = { Text("Restock") },
                                    onClick = {
                                        onRestockClick()
                                        menuExpanded = false
                                    },
                                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                                )
                                HorizontalDivider()
                                DropdownMenuItem(
                                    text = {
                                        Text("Delete", color = MaterialTheme.colorScheme.error)
                                    },
                                    onClick = {
                                        onDeleteClick()
                                        menuExpanded = false
                                    },
                                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                                )
                            }
                        }
                    }
                }
                // --- Item Details Section ---
                Column(modifier = Modifier.padding(12.dp)) {
                    // Item Name
                    Text(
                        text = item.name,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    // Category Tag
                    Surface(
                        shape = MaterialTheme.shapes.small,
                        border = BorderStroke(1.dp, Color.LightGray),
                        color = Color.Transparent,
                    ) {
                        Text(
                            text = item.category.uppercase(),
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.Gray,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    // --- Bottom Info Row: Price & Stock ---
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        // Price
                        Text(
                            text = "₱${item.price}",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color.Gray
                        )
                        // Stock Count
                        Text(
                            text = buildAnnotatedString {
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                    append(item.stock.toString())
                                }
                                append(" in Stock")
                            },
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.Gray
                        )
                    }
                }
            }
        }
    )
}
