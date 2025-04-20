package com.arnold.myapplication.screens

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
//import com.arnold.camerax.CameraController
import com.arnold.myapplication.camerax.CameraController
import com.arnold.myapplication.camerax.CameraController.CameraError
import com.arnold.myapplication.permissions.rememberCameraAndStoragePermissionState
import com.arnold.myapplication.utils.rememberImagePicker

//import com.arnold.myapplication.camerax.rememberImagePicker
//import com.arnold.myapplication.permissions.rememberCameraPermissionState
//import com.arnold.permissions.rememberCameraPermissionState

@Composable
fun CameraScreen(
    onImageSelected: (Bitmap) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraController = remember { CameraController(context, lifecycleOwner) }
    val hasPermissions = rememberCameraAndStoragePermissionState()
    var showError by remember { mutableStateOf<String?>(null) }
    var isCapturing by remember { mutableStateOf(false) }

    // Image Picker Setup
    val imagePicker = rememberImagePicker { uri ->
        try {
            context.contentResolver.openInputStream(uri)?.use { stream ->
                val bitmap = BitmapFactory.decodeStream(stream)
                bitmap?.let(onImageSelected) ?: run {
                    showError = "Failed to decode image"
                }
            } ?: run {
                showError = "Could not open image file"
            }
        } catch (e: Exception) {
            showError = "Error loading image: ${e.message}"
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        // Permission Check
        if (!hasPermissions) {
            PermissionRequestScreen()
            return
        }

        // Camera Preview
        AndroidView(
            factory = { ctx ->
                PreviewView(ctx).apply {
                    implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                    cameraController.initializeCamera(this) { error ->
                        showError = "Camera error: ${error.message}"
                    }
                }
            },
            modifier = Modifier.fillMaxSize()
        )

        // Bottom Controls
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Error Message
            if (showError != null) {
                Text(
                    text = showError ?: "",
                    color = Color.Red,
                    modifier = Modifier.padding(8.dp)
                )
            }

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // Gallery Button
                Button(
                    onClick = { imagePicker.launchGallery() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    ),
                    modifier = Modifier.size(60.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.PhotoLibrary,
                        contentDescription = "Select from gallery",
                        modifier = Modifier.size(24.dp)
                    )
                }

                // Capture Button
                Button(
                    onClick = {
                        if (!isCapturing) {
                            isCapturing = true
                            cameraController.captureImage(
                                onSuccess = { bitmap ->
                                    isCapturing = false
                                    onImageSelected(bitmap)
                                },
                                onError = { error ->
                                    isCapturing = false
                                    showError = when (error) {
                                        CameraError.CameraUnavailable -> "Camera unavailable"
                                        else -> "Capture failed"
                                    }
                                }
                            )
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier.size(60.dp),
                    enabled = !isCapturing
                ) {
                    if (isCapturing) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 3.dp
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Camera,
                            contentDescription = "Take photo",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PermissionRequestScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Permissions Required",
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Please grant camera and storage permissions in app settings",
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = {
                // Add intent to open app settings
            }
        ) {
            Text("Open Settings")
        }
    }
}