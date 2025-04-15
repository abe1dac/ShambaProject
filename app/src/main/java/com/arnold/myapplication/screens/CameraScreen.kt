package com.arnold.myapplication.screens

import android.graphics.Bitmap
import android.util.Log
import androidx.camera.view.PreviewView
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

    DisposableEffect(Unit) {
        onDispose {
            cameraController.cleanup()
        }
    }

    Column(modifier = modifier.fillMaxSize()) {
        if (showError != null) {
            Text(
                text = showError ?: "Unknown error",
                color = Color.Red,
                modifier = Modifier.padding(8.dp)
            )
        }

        if (hasPermission) {
            Box(modifier = Modifier.weight(1f)) {
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

                FloatingActionButton(
                    onClick = {
                        cameraController.captureImage(
                            onSuccess = { bitmap ->
                                onImageCaptured(bitmap)
                            },
                            onError = { error ->
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
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Camera permission required")
            }
        }
    }
}