package com.mobdeve.s18.roman.isaacnathan.alleymate.ui.catalogue.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mobdeve.s18.roman.isaacnathan.alleymate.common.components.modal.BaseModal

@Composable
fun DeleteCategoryModal(
    categoryName: String,
    isDeletable: Boolean,
    itemCount: Int,
    onDismissRequest: () -> Unit,
    onConfirmDelete: () -> Unit
) {
    BaseModal(
        onDismissRequest = onDismissRequest,
        headerTitle = "Delete Category"
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = if (isDeletable) {
                    "Are you sure you want to delete the '$categoryName' category?"
                } else {
                    "Cannot delete '$categoryName'."
                },
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )

            Text(
                text = if (isDeletable) {
                    "This action cannot be undone."
                } else {
                    "This category still contains $itemCount item(s). Please move or delete them before deleting the category."
                },
                style = MaterialTheme.typography.bodyMedium,
                color = if (isDeletable) Color(0xFFF44336) else Color.Gray,
                textAlign = TextAlign.Center
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onDismissRequest,
                    modifier = Modifier.weight(1f),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text(if (isDeletable) "CANCEL" else "OK")
                }

                if (isDeletable) {
                    Button(
                        onClick = onConfirmDelete,
                        modifier = Modifier.weight(1f),
                        shape = MaterialTheme.shapes.medium,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFF44336)
                        )
                    ) {
                        Text("DELETE", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}