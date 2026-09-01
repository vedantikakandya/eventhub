package com.example.eventhub.ui.profile

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.eventhub.data.model.vendor
import com.example.eventhub.ui.theme.Purple
import com.example.eventhub.viewmodel.vendorviewmodel
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BecomeVendorScreen(

    vendorViewModel: vendorviewmodel = viewModel(),
    onVendorCreated: () -> Unit = {}

) {

    var name by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var website by remember { mutableStateOf("") }
    var about by remember { mutableStateOf("") }

    var selectedCategory by remember {
        mutableStateOf("")
    }

    var expanded by remember {
        mutableStateOf(false)
    }

    val categories = listOf(
        "Photography",
        "Decoration",
        "Catering",
        "Music",
        "Venue",
        "Technology",
        "Cultural"
    )

    val context = androidx.compose.ui.platform.LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F2FA))
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {

        Spacer(modifier = Modifier.height(20.dp))

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {

            Box {

                Surface(
                    modifier = Modifier.size(140.dp),
                    shape = CircleShape,
                    color = Color.White,
                    shadowElevation = 8.dp
                ) {}

                FloatingActionButton(
                    onClick = {

                    },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .size(40.dp),
                    containerColor = Purple
                ) {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = null
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {

            Column(
                modifier = Modifier.padding(16.dp)
            ) {

                Text("Name")
                Spacer(modifier = Modifier.height(4.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text("Address")
                Spacer(modifier = Modifier.height(4.dp))

                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text("Phone Number")
                Spacer(modifier = Modifier.height(4.dp))

                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text("Website")
                Spacer(modifier = Modifier.height(4.dp))

                OutlinedTextField(
                    value = website,
                    onValueChange = { website = it },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text("About")
                Spacer(modifier = Modifier.height(4.dp))

                OutlinedTextField(
                    value = about,
                    onValueChange = { about = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text("Category")

                Spacer(modifier = Modifier.height(4.dp))

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = {
                        expanded = !expanded
                    }
                ) {

                    OutlinedTextField(
                        value = selectedCategory,
                        onValueChange = {},
                        readOnly = true,
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded)
                        }
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = {
                            expanded = false
                        }
                    ) {

                        categories.forEach { category ->

                            DropdownMenuItem(
                                text = {
                                    Text(category)
                                },
                                onClick = {
                                    selectedCategory = category
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        Button(
            onClick = {

                val uid =
                    FirebaseAuth.getInstance()
                        .currentUser?.uid ?: return@Button

                val vendor = vendor(

                    userid = uid,
                    name = name,
                    address = address,
                    number = phone.toLongOrNull() ?: 0,
                    description = about,
                    email = website,
                    category = selectedCategory
                )

                vendorViewModel.createVendor(
                    vendor = vendor,
                    onSuccess = {

                        Toast.makeText(
                            context,
                            "Vendor Created",
                            Toast.LENGTH_SHORT
                        ).show()

                        onVendorCreated()
                    }
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Purple
            )
        ) {

            Text(
                text = "Start Business",
                fontSize = 16.sp
            )
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}