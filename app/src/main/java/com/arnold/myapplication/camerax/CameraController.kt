package com.arnold.myapplication.camerax

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import java.io.File
import java.util.concurrent.Executors

class CameraController(
    private val context: Context,
    private val lifecycleOwner: LifecycleOwner
) {
    private var imageCapture: ImageCapture? = null
    private var cameraProvider: ProcessCameraProvider? = null
    private val cameraExecutor = Executors.newSingleThreadExecutor()

    sealed class CameraError {
        object CameraUnavailable : CameraError()
        object CaptureFailed : CameraError()
        object ImageProcessingFailed : CameraError()
        data class SystemError(val exception: Exception) : CameraError()
    }

    fun initializeCamera(previewView: PreviewView, onError: (Exception) -> Unit) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)

        cameraProviderFuture.addListener({
            try {
                cameraProvider = cameraProviderFuture.get()
                bindCameraUseCases(previewView)
            } catch (e: Exception) {
                onError(e)
            }
        }, ContextCompat.getMainExecutor(context))
    }

    private fun bindCameraUseCases(previewView: PreviewView) {
        val cameraProvider = cameraProvider ?: return

        val preview = Preview.Builder()
            .build()
            .also { it.setSurfaceProvider(previewView.surfaceProvider) }

        imageCapture = ImageCapture.Builder()
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
            .build()

        try {
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                lifecycleOwner,
                CameraSelector.DEFAULT_BACK_CAMERA,
                preview,
                imageCapture
            )
        } catch (e: Exception) {
            Log.e("CameraController", "Use case binding failed", e)
        }
    }

    fun captureImage(
        onSuccess: (Bitmap) -> Unit,
        onError: (CameraError) -> Unit
    ) {
        val imageCapture = imageCapture ?: run {
            onError(CameraError.CameraUnavailable)
            return
        }

        val file = File.createTempFile(
            "IMG_${System.currentTimeMillis()}",
            ".jpg",
            context.externalCacheDir
        ).takeIf { context.externalCacheDir != null } ?: run {
            onError(CameraError.SystemError(Exception("No cache directory available")))
            return
        }

        val outputOptions = ImageCapture.OutputFileOptions.Builder(file).build()

        imageCapture.takePicture(
            outputOptions,
            cameraExecutor,
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    try {
                        val bitmap = BitmapFactory.decodeFile(file.absolutePath)
                        bitmap?.let {
                            ContextCompat.getMainExecutor(context).execute {
                                onSuccess(it)
                            }
                        } ?: run {
                            ContextCompat.getMainExecutor(context).execute {
                                onError(CameraError.ImageProcessingFailed)
                            }
                        }
                    } catch (e: Exception) {
                        ContextCompat.getMainExecutor(context).execute {
                            onError(CameraError.SystemError(e))
                        }
                    }
                }

                override fun onError(exception: ImageCaptureException) {
                    val error = when (exception.imageCaptureError) {
                        ImageCapture.ERROR_FILE_IO ->
                            CameraError.SystemError(Exception("File I/O error"))
                        ImageCapture.ERROR_CAPTURE_FAILED ->
                            CameraError.CaptureFailed
                        ImageCapture.ERROR_CAMERA_CLOSED ->
                            CameraError.CameraUnavailable
                        ImageCapture.ERROR_INVALID_CAMERA ->
                            CameraError.CameraUnavailable
                        else ->
                            CameraError.SystemError(exception)
                    }
                    ContextCompat.getMainExecutor(context).execute {
                        onError(error)
                    }
                }
            }
        )
    }

    fun cleanup() {
        cameraExecutor.shutdown()
        cameraProvider?.unbindAll()
    }
}