//package com.arnold.permissions
package com.arnold.myapplication.permissions

import android.Manifest
import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

class PermissionManager(private val context: Context) {
    fun hasCameraPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        ) == android.content.pm.PackageManager.PERMISSION_GRANTED
    }
}

@Composable
fun rememberCameraPermissionState(): Boolean {
    val context = LocalContext.current
    val permissionManager = remember { PermissionManager(context) }
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { }

    SideEffect {
        if (!permissionManager.hasCameraPermission()) {
            launcher.launch(Manifest.permission.CAMERA)
        }
    }

    return permissionManager.hasCameraPermission()
}