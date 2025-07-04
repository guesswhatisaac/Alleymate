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
    BaseModal(
        onDismissRequest = onDismissRequest,
        headerTitle = "Edit Product"
    ) {
        val context = LocalContext.current
        var tempImageFile by remember { mutableStateOf<File?>(null) }

        // --- FORM STATE ---
        var productName by remember { mutableStateOf(item.name) }
        var productTag by remember { mutableStateOf(item.category) }
        var retailPrice by remember { mutableStateOf(item.price.toString()) }
        var imageUri by remember { mutableStateOf(item.imageUri?.toUri()) }

        // --- CAMERA AND PERMISSION LAUNCHERS ---
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

        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            FormImagePicker(
                onImagePickerClick = { permissionLauncher.launch(Manifest.permission.CAMERA) },
                imageUri = imageUri
            )

            FormTextField(
                label = "Product Name",
                value = productName,
                onValueChange = { productName = it }
            )

            FormDropdown(
                label = "Product Tag",
                selectedValue = productTag,
                options = itemCategories,
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