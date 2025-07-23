package com.mobdeve.s18.roman.isaacnathan.alleymate.ui.catalogue.components

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import com.mobdeve.s18.roman.isaacnathan.alleymate.common.components.modal.BaseModal
import com.mobdeve.s18.roman.isaacnathan.alleymate.common.components.modal.FormDropdown
import com.mobdeve.s18.roman.isaacnathan.alleymate.common.components.modal.FormImagePicker
import com.mobdeve.s18.roman.isaacnathan.alleymate.common.components.modal.FormTextField
import com.mobdeve.s18.roman.isaacnathan.alleymate.data.model.CatalogueItem
import com.mobdeve.s18.roman.isaacnathan.alleymate.theme.AlleyMainOrange
import java.io.File
import androidx.core.net.toUri
@Composable
fun EditProductModal(
    item: CatalogueItem,
    itemCategories: List<String>,
    onDismissRequest: () -> Unit,
    onConfirmEdit: (CatalogueItem) -> Unit
) {
    // Wrapper modal layout with header
    BaseModal(
        onDismissRequest = onDismissRequest,
        headerTitle = "Edit Product"
    ) {
        val context = LocalContext.current
        var tempImageFile by remember { mutableStateOf<File?>(null) }

        // --- Form State Variables ---
        var productName by remember { mutableStateOf(item.name) }
        var productTag by remember { mutableStateOf(item.category) }
        var retailPrice by remember { mutableStateOf(item.price.toString()) }
        var imageUri by remember { mutableStateOf(item.imageUri?.toUri()) }

        // --- Camera Launcher ---
        // Handles result after taking a picture using the camera
        val cameraLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.TakePicture()
        ) { success ->
            if (success) {
                imageUri = FileProvider.getUriForFile(
                    context,
                    "${context.packageName}.provider",
                    tempImageFile!!
                )
            }
        }

        // --- Permission Launcher ---
        // Requests camera permission and initiates camera capture if granted
        val permissionLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                val file = createImageFile(context)
                tempImageFile = file
                val uriForCamera = FileProvider.getUriForFile(
                    context,
                    "${context.packageName}.provider",
                    file
                )
                cameraLauncher.launch(uriForCamera)
            }
        }

        // --- Modal Form UI ---
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Image picker field
            FormImagePicker(
                onImagePickerClick = { permissionLauncher.launch(Manifest.permission.CAMERA) },
                imageUri = imageUri
            )

            // Input for product name
            FormTextField(
                label = "Product Name",
                value = productName,
                onValueChange = { productName = it }
            )

            // Dropdown for selecting product category/tag
            FormDropdown(
                label = "Product Tag",
                selectedValue = productTag,
                options = itemCategories,
                onValueChange = { productTag = it }
            )

            // Input for retail price (numeric only)
            FormTextField(
                label = "Retail Price",
                value = retailPrice,
                onValueChange = { retailPrice = it },
                keyboardType = KeyboardType.Number
            )

            // Submit button to save changes
            Button(
                onClick = {
                    val updatedItem = item.copy(
                        name = productName,
                        category = productTag,
                        price = retailPrice.toDoubleOrNull() ?: item.price,
                        imageUri = imageUri?.toString()
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
