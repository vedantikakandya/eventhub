package com.example.eventhub.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.eventhub.ui.theme.Purple
import com.example.eventhub.ui.theme.Purplemidium

@Composable
fun BottomBar(
    navController: NavController
) {

    // Light mode colors
    val selectedPurple = Purple
    val unselectedPurple = Purplemidium

    val items = listOf(
        bottomnavitem(
            route = screen.home.route,
            title = "Home",
            icon = Icons.Outlined.Home
        ),
        bottomnavitem(
            route = screen.explore.route,
            title = "Explore",
            icon = Icons.Default.Search
        ),
        bottomnavitem(
            route = screen.messages.route,
            title = "Messages",
            icon = Icons.Outlined.Email
        ),
        bottomnavitem(
            route = screen.profile.route,
            title = "Profile",
            icon = Icons.Default.Person
        )
    )

    NavigationBar(
        containerColor = Color.White
    ) {
        val navBackStackEntry = navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry.value?.destination?.route

        items.forEach { item ->
            val isSelected = currentRoute == item.route
            
            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.title
                    )
                },
                label = {
                    Text(text = item.title)
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = selectedPurple,
                    selectedTextColor = selectedPurple,
                    unselectedIconColor = unselectedPurple,
                    unselectedTextColor = unselectedPurple,
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}
