package com.example.eventhub.ui.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.eventhub.R

@Composable
fun onboard(    onGetStartedClick: () -> Unit,
                onLoginClick: () -> Unit
){
    Box(modifier= Modifier.fillMaxSize())
    {

        Image(painter = painterResource(id = R.drawable.onboardingimage),
            contentDescription = "onboarding Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize())
        Column(
            modifier= Modifier.fillMaxSize().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            Text(text="WELCOME BACK TO EVENTHUB!",
                fontSize = 15.sp,
                color= Color.White,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Find events, connect with people, and explore your campus like never before.",
                fontSize = 10.sp,
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.9f)
            )
            Spacer(modifier = Modifier.height(32.dp))
            OutlinedButton(onClick = onGetStartedClick,
                shape = RoundedCornerShape(12),
                modifier = Modifier.fillMaxWidth().height(39.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.White,
                )

            ) {
                Text(text = "Get Started",
                    fontSize = 12.sp,
                    color = Color.White
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = onLoginClick,
                shape = RoundedCornerShape(12),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(39.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    text = "Log In",
                    fontSize = 12.sp,
                    color = Color.White
                )
            }
        }

    }
}

//@Preview
//@Composable
//fun onboardPreview(){
//    onboard()
//}