package com.arnold.myapplication.navigation


import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search

sealed class Screen(val route: String, val title: String) {
    object Home : Screen("home", "Home")
    object Camera : Screen("camera", "Camera")
    object Results : Screen("results", "Results")
    object Search : Screen("search", "Search")
    object Profile : Screen("profile", "Profile")
}

sealed class BottomBarScreen(val route: String, val title: String, val icon: ImageVector) {
    object Home : BottomBarScreen("home", "Home", Icons.Filled.Home)
    object Search : BottomBarScreen("search", "Search", Icons.Filled.Search)
    object Profile : BottomBarScreen("profile", "Profile", Icons.Filled.Person)
}