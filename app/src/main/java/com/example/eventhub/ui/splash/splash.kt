package com.example.eventhub.ui.splash

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.eventhub.R
import com.example.eventhub.navigation.screen
import com.example.eventhub.viewmodel.authviewmodel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun splashscreen(navController: NavController,
                 viewmodel: authviewmodel= viewModel()){
    val user = FirebaseAuth.getInstance().currentUser

    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(2000)
        val loggedin=viewmodel.checkUserLoggedIn()
        if(loggedin){
            navController.navigate(screen.home.route )
        }else{
            navController.navigate(screen.onboard.route)
        }
        Log.d("AUTH_CHECK", "User = ${user?.uid}")

    }
    Box(modifier= Modifier.fillMaxSize())
    {
        Image(painter = painterResource(id = R.drawable.splashimage),
            contentDescription = "Splash Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize())

    }
}

//@Preview
//@Composable
//fun splashscreenpreview(){
//    splashscreen(navController = NavController(context = null))
//
//}