package com.arnold.myapplication.navigation

import android.graphics.Bitmap
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.arnold.myapplication.ml.Classifier
import com.arnold.myapplication.screens.CameraScreen
import com.arnold.myapplication.screens.DetectionScreen
import com.arnold.myapplication.screens.HomeScreen
import com.arnold.myapplication.screens.LoginScreen
import com.arnold.myapplication.screens.ProfileScreen
import com.arnold.myapplication.screens.ResultScreen
import com.arnold.myapplication.screens.SearchScreen
import java.io.ByteArrayOutputStream

@Composable
fun NavigationGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
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
                onNavigateToResult = { results ->
                    val resultsString = results.joinToString("|") { "${it.title},${it.confidence}" }
                    navController.navigate(Screen.Result.createRoute(resultsString)) {
                        popUpTo("camera") { inclusive = true }
                    }
                },
            )
        }
        composable(
            Screen.Result.route,
            arguments = listOf(navArgument("results") { type = NavType.StringType })
        ) { backStackEntry ->
            val resultsString = backStackEntry.arguments?.getString("results") ?: ""
            val results = parseResults(resultsString)

            ResultScreen(results = results)
        }
    }
}

private fun parseResults(resultsString: String): List<Classifier.Result> {
    if (resultsString.isBlank()) {
        return emptyList()
    }
    return resultsString.split("|").mapNotNull { resultString ->
        val parts = resultString.split(",")
        if (parts.size == 2) {
            try {
                Classifier.Result(parts[0], parts[1].toFloat())
            } catch (e: NumberFormatException) {
                // Handle cases where the second part is not a valid float
                // Log the error, return null or handle in another way
                null
            }
        } else {
            // Handle cases where the result string is malformed
            // Log the error, return null or handle in another way
            null
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



