package com.example.eventhub.navigation


import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.eventhub.ui.theme.Purple
import com.example.eventhub.ui.theme.Purplemidium
import com.example.eventhub.viewmodel.vendorviewmodel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun ProfileScreen(
    navController: NavController,
    vendorViewModel: vendorviewmodel =
        viewModel()

) {

    val isVendor by
    vendorViewModel.isVendor.collectAsState()

    val context =
        LocalContext.current
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {

        val uid =
            FirebaseAuth.getInstance().currentUser?.uid
                ?: return@LaunchedEffect

        FirebaseFirestore.getInstance()
            .collection("users")
            .document(uid)
            .get()
            .addOnSuccessListener {

                name = it.getString("name") ?: ""
                email = it.getString("email") ?: ""
            }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Spacer(modifier = Modifier.height(24.dp))

        Surface(
            modifier = Modifier
                .size(100.dp)
                .align(Alignment.CenterHorizontally),
            shape = CircleShape,
            color = Color.LightGray
        ) {}

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = name,
            fontSize = 20.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Text(
            text = email,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                navController.navigate(screen.editprofile.route)
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Edit Profile", color = Color.White)
        }

        Spacer(modifier = Modifier.height(24.dp))

        ProfileMenuItem(
            title = "Become Vendor",
            icon = Icons.Default.Person
        ) {
            navController.navigate(screen.becomevendor.route)
        }

        ProfileMenuItem(
            title = "My Events",
            icon = Icons.Default.DateRange
        ) {
            navController.navigate(screen.myevents.route)
        }

        ProfileMenuItem(
            title = "My Business",
            icon = Icons.Default.Build
        ) {
            vendorViewModel.checkIfVendor { isVendor ->

                if (isVendor) {

                    navController.navigate(
                        screen.mybusiness.route
                    )

                } else {

                    Toast.makeText(
                        context,
                        "First register yourself as a vendor",
                        Toast.LENGTH_SHORT
                    ).show()

                    navController.navigate(
                        screen.becomevendor.route
                    )
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        ProfileMenuItem(
            title = "Logout",
            icon = Icons.Default.Lock
        ) {

            FirebaseAuth.getInstance().signOut()

            navController.navigate(screen.login.route) {
                popUpTo(0)
            }
        }
    }
}


@Composable
fun ProfileMenuItem(
    title: String,
    icon: ImageVector,
    onClick: () -> Unit
) {

    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(16.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                imageVector = icon,
                tint = Purple,
                contentDescription = null
            )

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = title,
                modifier = Modifier.weight(1f)
            )

            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = null,
                tint = Purple

            )
        }
    }
}