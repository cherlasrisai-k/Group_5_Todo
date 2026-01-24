package com.example.todo.ui

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomBar(nav: NavHostController) {

    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Active,
        BottomNavItem.History
    )

    NavigationBar {

        val backStack by nav.currentBackStackEntryAsState()
        val current = backStack?.destination?.route

        items.forEach { item ->
            NavigationBarItem(
                selected = current == item.route,
                onClick = {
                    nav.navigate(item.route) {
                        popUpTo("home")
                        launchSingleTop = true
                    }
                },
                icon = { Icon(item.icon, null) },
                label = { Text(item.title) }
            )
        }
    }
}