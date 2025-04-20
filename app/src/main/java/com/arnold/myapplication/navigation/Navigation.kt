package com.arnold.myapplication.navigation

import android.R.attr.type
import android.graphics.Bitmap
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.arnold.myapplication.screens.*
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.arnold.myapplication.screens.*
import java.io.ByteArrayOutputStream
import kotlin.io.encoding.Base64

@Composable
fun NavigationGraph(navController: NavHostController,
                    modifier: Modifier = Modifier) {
    var capturedImage by remember { mutableStateOf<Bitmap?>(null) }
    NavHost(navController, startDestination = BottomBarScreen.Home.route, modifier = modifier) {
        composable(BottomBarScreen.Home.route) {
            HomeScreen(
                onCaptureImage = {
                    navController.navigate(Screen.Camera.route)
                }
            )
        }
       // composable(BottomBarScreen.Search.route) { SearchScreen() }
       //composable(BottomBarScreen.Profile.route) { ProfileScreen() }

        composable(BottomBarScreen.Search.route) {
            SearchScreen(navController = navController)
        }

        composable(
            route = Screen.Detection.route,
            arguments = listOf(
                navArgument("diseaseId") {
                    type = NavType.IntType
                    defaultValue = 0
                }
            )
        ) { backStackEntry ->
            val diseaseId = backStackEntry.arguments?.getInt("diseaseId") ?: 0
            DetectionScreen(
                diseaseId = diseaseId,
                navController = navController
            )
        }
      composable(BottomBarScreen.Profile.route) {
            ProfileScreen(navController)
        }

        // In your NavHost setup:

        composable(Screen.Camera.route) {
            CameraScreen(
                onImageSelected = { bitmap: Bitmap ->
                    // Explicit Bitmap type declaration
                    val base64String = bitmapToBase64(bitmap)
                    navController.navigate(
                        Screen.Results.route.replace("{imageData}", base64String)
                    )
                }
            )
        }
        composable(Screen.Results.route) {
            ResultsScreen(
                disease = "Early Blight",
                treatment = "Apply fungicide and remove infected leaves."
            )
        }
    }
}



// Update the helper function:
private fun bitmapToBase64(bitmap: Bitmap): String {
    val byteArrayOutputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
    val byteArray = byteArrayOutputStream.toByteArray()
    return android.util.Base64.encodeToString(byteArray, android.util.Base64.DEFAULT)
}



