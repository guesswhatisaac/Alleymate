package com.mobdeve.s18.roman.isaacnathan.alleymate.ui.catalogue.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.mobdeve.s18.roman.isaacnathan.alleymate.common.components.modal.BaseModal
import com.mobdeve.s18.roman.isaacnathan.alleymate.common.components.modal.FormDropdown
import com.mobdeve.s18.roman.isaacnathan.alleymate.common.components.modal.FormImagePicker
import com.mobdeve.s18.roman.isaacnathan.alleymate.common.components.modal.FormTextField
import com.mobdeve.s18.roman.isaacnathan.alleymate.theme.AlleyMainOrange
import android.Manifest
import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import java.io.File

fun createImageFile(context: Context): File {
    val timeStamp = System.currentTimeMillis()
    val storageDir: File? = context.cacheDir
    return File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir)
}

@Composable
fun AddProductModal(
    itemCategories: List<String>,
    onDismissRequest: () -> Unit,
    onAddProduct: (name: String, category: String, price: Double, stock: Int, imageUri: String?) -> Unit
) {
    BaseModal(
        onDismissRequest = onDismissRequest,
        headerTitle = "Add Product"
    ) {
        var productName by remember { mutableStateOf("") }
        var productTag by remember { mutableStateOf(itemCategories.firstOrNull() ?: "N/A") }
        var retailPrice by remember { mutableStateOf("") }
        var initialStock by remember { mutableStateOf("") }
        var isPriceError by remember { mutableStateOf(false) }
        var isStockError by remember { mutableStateOf(false) }

        val context = LocalContext.current
        var hasCameraPermission by remember { mutableStateOf(false) }
        var imageUri by remember { mutableStateOf<Uri?>(null) }
        var tempImageFile by remember { mutableStateOf<File?>(null) }

        // --- Camera Launcher ---
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
        val permissionLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            hasCameraPermission = isGranted
            if (isGranted) {
                val file = createImageFile(context)
                tempImageFile = file
                val uri = FileProvider.getUriForFile(
                    context,
                    "${context.packageName}.provider",
                    file
                )
                cameraLauncher.launch(uri)
            }
        }

        // Check for permission on compose start
        LaunchedEffect(Unit) {}

        val isFormValid by remember(productName, retailPrice, initialStock) {
            derivedStateOf {
                productName.isNotBlank() &&
                        retailPrice.toDoubleOrNull() != null &&
                        initialStock.toIntOrNull() != null
            }
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            FormImagePicker(
                imageUri = imageUri,
                onImagePickerClick = {
                    permissionLauncher.launch(Manifest.permission.CAMERA)
                }
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

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                FormTextField(
                    label = "Retail Price",
                    value = retailPrice,
                    onValueChange = {
                        retailPrice = it
                        isPriceError = it.toDoubleOrNull() == null && it.isNotBlank()
                    },
                    modifier = Modifier.weight(1f),
                    keyboardType = KeyboardType.Number,
                    isError = isPriceError
                )
                FormTextField(
                    label = "Initial Stock",
                    value = initialStock,
                    onValueChange = {
                        initialStock = it
                        isStockError = it.toDoubleOrNull() == null && it.isNotBlank()
                    },
                    modifier = Modifier.weight(1f),
                    keyboardType = KeyboardType.Number,
                    isError = isStockError
                )
            }

            Button(
                onClick = {
                    onAddProduct(
                        productName,
                        productTag,
                        retailPrice.toDouble(),
                        initialStock.toInt(),
                        imageUri?.toString()
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = isFormValid,
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

