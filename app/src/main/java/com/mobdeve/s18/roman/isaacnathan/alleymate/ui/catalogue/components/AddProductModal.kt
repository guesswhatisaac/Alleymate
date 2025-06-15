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
import com.mobdeve.s18.roman.isaacnathan.alleymate.theme.AlleyMainOrange
import com.mobdeve.s18.roman.isaacnathan.alleymate.theme.AlleyMateTheme

@Composable
fun AddProductModal(
    onDismissRequest: () -> Unit,
    onAddProduct: () -> Unit
) {
    BaseModal(
        onDismissRequest = onDismissRequest,
        headerTitle = "Add Product"
    ) {
        var productName by remember { mutableStateOf("MHYLOW Sticker") }
        var productTag by remember { mutableStateOf("Sticker") }
        var retailPrice by remember { mutableStateOf("30") }
        var initialStock by remember { mutableStateOf("30") }

        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            FormImagePicker(onImagePickerClick = { /* TODO */ })

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

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                FormTextField(
                    label = "Retail Price",
                    value = retailPrice,
                    onValueChange = { retailPrice = it },
                    modifier = Modifier.weight(1f),
                    keyboardType = KeyboardType.Number
                )
                FormTextField(
                    label = "Initial Stock",
                    value = initialStock,
                    onValueChange = { initialStock = it },
                    modifier = Modifier.weight(1f),
                    keyboardType = KeyboardType.Number
                )
            }

            Button(
                onClick = onAddProduct,
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                colors = ButtonDefaults.buttonColors(containerColor = AlleyMainOrange)
            ) {
                Text(
                    text = "ADD",
                    modifier = Modifier.padding(vertical = 8.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Preview
@Composable
private fun AddProductModalPreview() {
    AlleyMateTheme {
        AddProductModal(onDismissRequest = {}, onAddProduct = {})
    }
}