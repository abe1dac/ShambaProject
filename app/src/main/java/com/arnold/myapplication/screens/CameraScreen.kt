package com.arnold.myapplication.screens

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.CheckCircle
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
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.arnold.myapplication.camerax.CameraController
import com.arnold.myapplication.camerax.CameraController.CameraError
import com.arnold.myapplication.permissions.rememberCameraAndStoragePermissionState
import com.arnold.myapplication.utils.rememberImagePicker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
    var captureState by remember { mutableStateOf<CaptureState>(CaptureState.Idle) }
    val coroutineScope = rememberCoroutineScope()

    // Image Picker with loading state
    val imagePicker = rememberImagePicker { uri ->
        coroutineScope.launch { // Launch a coroutine here
            captureState = CaptureState.Uploading
            try {
                context.contentResolver.openInputStream(uri)?.use { stream ->
                    val bitmap = BitmapFactory.decodeStream(stream) // No withContext needed here
                    bitmap?.let {
                        onImageSelected(it)
                        captureState = CaptureState.Success
                    } ?: run {
                        showError = "Failed to decode image"
                        captureState = CaptureState.Idle
                    }
                } ?: run {
                    showError = "Could not open image"
                    captureState = CaptureState.Idle
                }
            } catch (e: Exception) {
                showError = "Error: ${e.message}"
                captureState = CaptureState.Idle
            }
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        // Camera Preview (when not uploading)
        if (captureState !is CaptureState.Uploading && hasPermissions) {
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
        }

        // Loading Overlay
        when (captureState) {
            is CaptureState.Capturing -> {
                FullScreenLoadingIndicator("Capturing image...")
            }
            is CaptureState.Uploading -> {
                FullScreenLoadingIndicator("Processing image...")
            }
            is CaptureState.Success -> {
                // Brief success state before navigation
                LaunchedEffect(Unit) {
                    delay(500) // Show success for 0.5s
                    captureState = CaptureState.Idle
                }
                FullScreenSuccessIndicator()
            }
            else -> {}
        }

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

            // Action Buttons (disabled during operations)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // Gallery Button
                Button(
                    onClick = {
                        if (captureState == CaptureState.Idle) {
                            imagePicker.launchGallery()
                        }
                    },
                    enabled = captureState == CaptureState.Idle,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(
                            alpha = if (captureState == CaptureState.Idle) 1f else 0.5f
                        )
                    ),
                    modifier = Modifier.size(60.dp)
                ) {
                    if (captureState is CaptureState.Uploading) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.PhotoLibrary,
                            contentDescription = "Gallery",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                // Capture Button
                Button(
                    onClick = {
                        if (captureState == CaptureState.Idle) {
                            captureState = CaptureState.Capturing
                            cameraController.captureImage(
                                onSuccess = { bitmap ->
                                    onImageSelected(bitmap)
                                    captureState = CaptureState.Success
                                },
                                onError = { error ->
                                    showError = when (error) {
                                        CameraError.CameraUnavailable -> "Camera unavailable"
                                        else -> "Capture failed"
                                    }
                                    captureState = CaptureState.Idle
                                }
                            )
                        }
                    },
                    enabled = captureState == CaptureState.Idle,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary.copy(
                            alpha = if (captureState == CaptureState.Idle) 1f else 0.5f
                        )
                    ),
                    modifier = Modifier.size(60.dp)
                ) {
                    when (captureState) {
                        is CaptureState.Capturing -> {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(24.dp),
                                strokeWidth = 2.dp
                            )
                        }
                        else -> {
                            Icon(
                                imageVector = Icons.Default.Camera,
                                contentDescription = "Capture",
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

// State Sealed Class
sealed class CaptureState {
    object Idle : CaptureState()
    object Capturing : CaptureState()
    object Uploading : CaptureState()
    object Success : CaptureState()
}

// Loading Components
@Composable
private fun FullScreenLoadingIndicator(text: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.7f)),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator(
                color = Color.White,
                modifier = Modifier.size(48.dp),
                strokeWidth = 4.dp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = text,
                color = Color.White,
                fontSize = 18.sp
            )
        }
    }
}

@Composable
private fun FullScreenSuccessIndicator() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0x8000AA00)), // Semi-transparent green
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = "Success",
            tint = Color.White,
            modifier = Modifier.size(72.dp)
        )
    }
}