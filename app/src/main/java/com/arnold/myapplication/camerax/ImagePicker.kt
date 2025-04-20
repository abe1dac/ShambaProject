package com.arnold.myapplication.utils

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext

class ImagePickerState(
    val imageUri: Uri?,
    val launchGallery: () -> Unit
)

@Composable
fun rememberImagePicker(onImagePicked: (Uri) -> Unit): ImagePickerState {
    val context = LocalContext.current
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            uri?.let {
                imageUri = it
                onImagePicked(it)
            }
        }
    )

    return ImagePickerState(
        imageUri = imageUri,
        launchGallery = { galleryLauncher.launch("image/*") }
    )
}