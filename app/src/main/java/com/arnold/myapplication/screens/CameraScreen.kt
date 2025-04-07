package com.arnold.myapplication.screens


import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.view.PreviewView
import androidx.compose.runtime.remember
import androidx.core.content.ContextCompat
import java.io.File
import java.util.logging.Logger

@Composable
fun CameraScreen(onImageCaptured: (Bitmap) -> Unit) {
    // ... your existing CameraScreen implementation

        val context = LocalContext.current
        val lifecycleOwner = LocalLifecycleOwner.current

        val imageCapture = remember { ImageCapture.Builder().build() }

        Box(modifier = Modifier.fillMaxSize()) {
            AndroidView(
                factory = { ctx ->
                    PreviewView(ctx).apply {
                        implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                    }
                },
                modifier = Modifier.fillMaxSize()
            )

            Button(
                onClick = {
                    val file = File.createTempFile("IMG_", ".jpg", context.cacheDir)
                    val outputOptions = ImageCapture.OutputFileOptions.Builder(file).build()

                    imageCapture.takePicture(
                        outputOptions,
                        ContextCompat.getMainExecutor(context),
                        object : ImageCapture.OnImageSavedCallback {
                            override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                                val bitmap = BitmapFactory.decodeFile(file.absolutePath)
                                onImageCaptured(bitmap)
                            }

                            override fun onError(exception: ImageCaptureException) {
                                Logger.getLogger("CameraX").severe("Image capture failed: ${exception.message}")
                            }
                        }
                    )
                },
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                Text("Capture")
            }
        }
    }
