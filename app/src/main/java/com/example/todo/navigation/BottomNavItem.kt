package com.example.todo.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val screen: Routes,
    val title: String,
    val icon: ImageVector
) {
    object Home : BottomNavItem(Routes.HOME, "Home", Icons.Default.Home)
    object Active : BottomNavItem(Routes.ACTIVE, "Active", Icons.Default.List)
    object History : BottomNavItem(Routes.HISTORY, "History", Icons.Default.CheckCircle)
}