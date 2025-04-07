package com.arnold.myapplication.navigation

import android.graphics.Bitmap
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.arnold.myapplication.screens.*

@Composable
fun NavigationGraph(navController: NavHostController, modifier: Modifier = Modifier) {
    var capturedImage by remember { mutableStateOf<Bitmap?>(null) }
    NavHost(navController, startDestination = BottomBarScreen.Home.route, modifier = modifier) {
        composable(BottomBarScreen.Home.route) {
            HomeScreen(
                onCaptureImage = {
                    navController.navigate(Screen.Camera.route)
                }
            )
        }
        composable(BottomBarScreen.Search.route) { SearchScreen() }
       //composable(BottomBarScreen.Profile.route) { ProfileScreen() }
      composable(BottomBarScreen.Profile.route) {
            ProfileScreen(navController)
        }
        composable(Screen.Camera.route) {
            CameraScreen { bitmap ->
                capturedImage = bitmap
                navController.navigate(Screen.Results.route)
            }
        }
        composable(Screen.Results.route) {
            ResultsScreen(
                disease = "Early Blight",
                treatment = "Apply fungicide and remove infected leaves."
            )
        }
    }
}