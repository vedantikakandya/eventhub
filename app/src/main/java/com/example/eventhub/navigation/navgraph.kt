package com.example.eventhub.navigation

import androidx.navigation.compose.*
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.eventhub.ui.messages.ChatScreen
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.example.eventhub.ui.auth.login
import com.example.eventhub.ui.auth.onboard
import com.example.eventhub.ui.auth.signup
import com.example.eventhub.ui.event.CreateEventScreen
import com.example.eventhub.ui.event.eventdetails
import com.example.eventhub.ui.home.homescreen
import com.example.eventhub.ui.profile.BecomeVendorScreen
import com.example.eventhub.ui.profile.EditProfileScreen
import com.example.eventhub.ui.profile.MyBusinessScreen
import com.example.eventhub.ui.profile.MyEventsScreen
import com.example.eventhub.ui.splash.splashscreen
import com.example.eventhub.ui.vendor.vendorprofile

@Composable
fun appnav(navController: NavHostController){
    NavHost(
        navController = navController,
        startDestination = screen.Splash.route
    ){
        composable(screen.Splash.route){
            splashscreen(navController)
        }
        composable(screen.onboard.route){
            onboard(onGetStartedClick = {
                navController.navigate(screen.signup.route)
            }, onLoginClick = {
                navController.navigate(screen.login.route)
            })
        }
        composable(screen.login.route){
            login(onLoginClick = {
                navController.navigate(screen.home.route){
                    popUpTo(screen.login.route) { inclusive = true }
                }
            },
                onSignupClick ={
                    navController.navigate(screen.signup.route)
                }
            )
        }
        composable(screen.signup.route){
            signup(onLoginClick = {
                navController.navigate(screen.login.route)
            },
                onSignupClick ={
                    navController.navigate(screen.home.route){
                        popUpTo(screen.signup.route) { inclusive = true }
                    }
                }
            )
        }
        composable(screen.home.route) {
            homescreen(onEventClick = { eventId ->
                navController.navigate(screen.eventdetails.createRoute(eventId))
            },
                onVendorClick = { vendorId ->
                    navController.navigate(screen.vendordetails.createRoute(vendorId))
                }
            )
        }
        composable(screen.eventdetails.route) { backStackEntry ->

            val eventId =
                backStackEntry.arguments?.getString("eventId") ?: ""

            eventdetails(
                eventId = eventId,

                onChatClick = { chatId ->

                    navController.navigate(
                        screen.chat.createRoute(chatId)
                    )
                }
            )
        }

        composable(screen.vendordetails.route) { backStackEntry ->
            val vendorId = backStackEntry.arguments?.getString("vendorId")
            vendorprofile(vendorId = vendorId ?: "")
        }
        composable(screen.explore.route) {
            ExploreScreen(
                onEventClick = { eventId ->
                    navController.navigate(screen.eventdetails.createRoute(eventId))
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
        composable(screen.messages.route) {

            MessagesScreen(
                onChatClick = { chatId ->

                    navController.navigate(
                        screen.chat.createRoute(chatId)
                    )
                }
            )
        }

        composable(
            route = screen.chat.route,
            arguments = listOf(
                navArgument("chatId") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->

            val chatId =
                backStackEntry.arguments?.getString("chatId") ?: ""

            ChatScreen(
                chatId = chatId,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(screen.profile.route) {
            ProfileScreen( navController)
        }
        composable(screen.editprofile.route) {
            EditProfileScreen()
        }

        composable(screen.becomevendor.route) {
            BecomeVendorScreen(
                onVendorCreated = {
                    navController.navigate(screen.mybusiness.route){
                        popUpTo(screen.becomevendor.route) { inclusive = true }
                    }
                }
            )
        }

        composable(screen.myevents.route) {
            MyEventsScreen(
                onEventClick = { eventId ->
                    navController.navigate(
                        screen.eventdetails.createRoute(eventId)
                    )
                }
            )
        }

        composable(
            route = screen.createevent.route,
            arguments = listOf(
                navArgument("eventId") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            )
        ) { backStackEntry ->

            val eventId =
                backStackEntry.arguments
                    ?.getString("eventId")

            CreateEventScreen(
                eventId = eventId
            )
        }

        composable(
            screen.mybusiness.route
        ) {

            MyBusinessScreen(

                onEditEventClick = { eventId ->

                    navController.navigate(
                        screen.createevent
                            .createRoute(eventId)
                    )
                },

                onCreateEventClick = {
                    navController.navigate(
                        screen.createevent.route
                    )

                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }


    }
}