package com.example.eventhub

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.eventhub.navigation.BottomBar
import com.example.eventhub.navigation.appnav
import com.example.eventhub.navigation.screen
import com.example.eventhub.ui.theme.EventhubTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EventhubTheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                // Define screens where the BottomBar should be visible
                val bottomBarScreens = listOf(
                    screen.home.route,
                    screen.explore.route,
                    screen.messages.route,
                    screen.profile.route
                )

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        // Only show BottomBar on main app screens
                        if (currentRoute in bottomBarScreens) {
                            BottomBar(navController = navController)
                        }
                    }
                ) { innerPadding ->
                    // Wrap appnav in a Box with innerPadding to respect Scaffold's slots
                    Box(modifier = Modifier.padding(innerPadding)) {
                        appnav(navController = navController)
                    }
                }
            }
        }
    }
}
