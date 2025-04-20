package com.arnold.myapplication.screens

import android.graphics.Bitmap
import android.util.Log
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
//import com.arnold.camerax.CameraController
import com.arnold.myapplication.camerax.CameraController
import com.arnold.myapplication.permissions.rememberCameraPermissionState
//import com.arnold.permissions.rememberCameraPermissionState

@Composable
fun CameraScreen(
    onImageCaptured: (Bitmap) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraController = remember { CameraController(context, lifecycleOwner) }
    val hasPermission = rememberCameraPermissionState()
    var showError by remember { mutableStateOf<String?>(null) }
    var isCapturing by remember { mutableStateOf(false) }

    DisposableEffect(Unit) {
        onDispose {
            cameraController.cleanup()
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        // Show error if present
        if (showError != null) {
            Text(
                text = showError ?: "Unknown error",
                color = Color.Red,
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.TopCenter)
            )
        }

        if (hasPermission) {
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

            // Capture Button (conditionally rendered)
            if (!isCapturing) {
                FloatingActionButton(
                    onClick = {
                        isCapturing = true
                        cameraController.captureImage(
                            onSuccess = { bitmap ->
                                isCapturing = false
                                onImageCaptured(bitmap)
                            },
                            onError = { error ->
                                isCapturing = false
                                showError = when (error) {
                                    CameraController.CameraError.CameraUnavailable ->
                                        "Camera unavailable"
                                    CameraController.CameraError.CaptureFailed ->
                                        "Failed to capture image"
                                    CameraController.CameraError.ImageProcessingFailed ->
                                        "Failed to process image"
                                    is CameraController.CameraError.SystemError ->
                                        "Error: ${error.exception.message}"
                                }
                            }
                        )
                    },
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 32.dp),
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(
                        imageVector = Icons.Default.Camera,
                        contentDescription = "Capture image"
                    )
                }
            }
        } else {
            // Permission required message
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Camera permission required")
            }
        }

        // Full-screen overlay during capture
        if (isCapturing) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    CircularProgressIndicator(
                        color = Color.White,
                        strokeWidth = 4.dp,
                        modifier = Modifier.size(48.dp)
                    )
                    Text(
                        text = "Processing image...😚",
                        color = Color.White,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }
}
