package com.mobdeve.s18.roman.isaacnathan.alleymate.ui.catalogue.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mobdeve.s18.roman.isaacnathan.alleymate.common.components.modal.BaseModal
import com.mobdeve.s18.roman.isaacnathan.alleymate.common.components.modal.FormTextField
import com.mobdeve.s18.roman.isaacnathan.alleymate.theme.AlleyMainOrange

/**
 * Modal for adding a new item category.
 *
 * @param onDismissRequest Callback triggered when the modal is dismissed.
 * @param onAddCategory Callback triggered when a valid category name is submitted.
 */
@Composable
fun AddCategoryModal(
    onDismissRequest: () -> Unit,
    onAddCategory: (String) -> Unit
) {
    // Reusable base modal UI
    BaseModal(
        onDismissRequest = onDismissRequest,
        headerTitle = "Add Category"
    ) {
        // Local state for input text
        var categoryName by remember { mutableStateOf("") }

        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Text field for entering the category name
            FormTextField(
                label = "Category Name",
                value = categoryName,
                onValueChange = { categoryName = it }
            )

            // Button to add the category
            Button(
                onClick = {
                    onAddCategory(categoryName)
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = categoryName.isNotBlank(), // Disable button if input is blank
                shape = MaterialTheme.shapes.medium,
                colors = ButtonDefaults.buttonColors(containerColor = AlleyMainOrange)
            ) {
                Text(
                    text = "ADD CATEGORY",
                    modifier = Modifier.padding(vertical = 8.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
