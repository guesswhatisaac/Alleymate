package com.mobdeve.s18.roman.isaacnathan.alleymate.ui.catalogue.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mobdeve.s18.roman.isaacnathan.alleymate.common.components.modal.BaseModal
import com.mobdeve.s18.roman.isaacnathan.alleymate.common.components.modal.FormDropdown
import com.mobdeve.s18.roman.isaacnathan.alleymate.common.components.modal.FormImagePicker
import com.mobdeve.s18.roman.isaacnathan.alleymate.common.components.modal.FormTextField
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.CatalogueItem
import com.mobdeve.s18.roman.isaacnathan.alleymate.theme.AlleyMainOrange
import com.mobdeve.s18.roman.isaacnathan.alleymate.theme.AlleyMateTheme

@Composable
fun EditProductModal(
    item: CatalogueItem,
    onDismissRequest: () -> Unit,
    onConfirmEdit: (CatalogueItem) -> Unit // Passes the updated item back
) {
    BaseModal(
        onDismissRequest = onDismissRequest,
        headerTitle = "Edit Product"
    ) {
        // State for the form fields, initialized with the existing item's data
        var productName by remember { mutableStateOf(item.name) }
        var productTag by remember { mutableStateOf(item.category) }
        var retailPrice by remember { mutableStateOf(item.price.toString()) }

        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            FormImagePicker(onImagePickerClick = { /* TODO: Handle image selection */ })

            FormTextField(
                label = "Product Name",
                value = productName,
                onValueChange = { productName = it }
            )

            FormDropdown(
                label = "Product Tag",
                selectedValue = productTag,
                options = listOf("Sticker", "Print", "Jewelry"),
                onValueChange = { productTag = it }
            )

            FormTextField(
                label = "Retail Price",
                value = retailPrice,
                onValueChange = { retailPrice = it },
                keyboardType = KeyboardType.Number
            )

            Button(
                onClick = {
                    // Create an updated item object and pass it back
                    val updatedItem = item.copy(
                        name = productName,
                        category = productTag,
                        price = retailPrice.toIntOrNull() ?: item.price
                    )
                    onConfirmEdit(updatedItem)
                },
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                colors = ButtonDefaults.buttonColors(containerColor = AlleyMainOrange)
            ) {
                Text(
                    text = "SAVE CHANGES",
                    modifier = Modifier.padding(vertical = 8.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Preview
@Composable
private fun EditProductModalPreview() {
    AlleyMateTheme {
        EditProductModal(
            item = CatalogueItem(1, "MHYLOW star sticker", "Sticker", 100, 50),
            onDismissRequest = {},
            onConfirmEdit = {}
        )
    }
}