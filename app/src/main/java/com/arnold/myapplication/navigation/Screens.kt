package com.arnold.myapplication.navigation

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search

sealed class Screen(val route: String) {
    object Login:Screen("login")
    object Home : Screen("home")
    object Camera : Screen("camera")
    object Search : Screen("search")
    object Profile : Screen("profile")
    object Detection : Screen("detection/{diseaseId}") {
        fun createRoute(diseaseId: Int) = "detection/$diseaseId"
    }
    object Result : Screen("result/{results}") {
        fun createRoute(results: String) = "result/$results"
    }
}

sealed class BottomBarScreen(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Home : BottomBarScreen(Screen.Home.route, "Home", Icons.Filled.Home)
    object Search : BottomBarScreen(Screen.Search.route, "Search", Icons.Filled.Search)
    object Profile : BottomBarScreen(Screen.Profile.route, "Profile", Icons.Filled.Person)
}