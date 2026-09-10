package com.example.eventhub.ui.vendor

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.eventhub.R
import com.example.eventhub.data.model.vendor
import com.example.eventhub.ui.theme.Purple
import com.example.eventhub.viewmodel.MessageViewModel
import com.example.eventhub.viewmodel.vendorviewmodel

@Composable
fun vendorprofile(
    vendor: vendor? = null,
    vendorId: String,
    vendorViewModel: vendorviewmodel = viewModel(),
    messageViewModel: MessageViewModel = viewModel(),
    onChatClick: (String) -> Unit = {}
) {
    val context = LocalContext.current
    val selectedVendor by vendorViewModel.selectedVendor.collectAsState()
    val displayedVendor = vendor ?: if (selectedVendor.vendorId == vendorId) selectedVendor else vendor()

    LaunchedEffect(vendorId) {
        if (vendor == null) {
            vendorViewModel.fetchVendorById(vendorId)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Cover Image
        Image(
            painter = painterResource(id = R.drawable.vendor_placeholder),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp),
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .offset(y = (-45).dp)
                    .size(90.dp),
                shape = CircleShape,
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.vendorimage_placeholder),
                    contentDescription = displayedVendor.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = displayedVendor.name,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = displayedVendor.description,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Contact Information",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Email",
                fontWeight = FontWeight.Medium
            )

            Text(
                text = displayedVendor.email,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Phone",
                fontWeight = FontWeight.Medium
            )

            Text(
                text = displayedVendor.number.toString(),
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Statistics",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(10.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Events Hosted",
                        color = Color.Gray
                    )

                    Text(
                        text = displayedVendor.eventshosted.toString(),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    val vendorUserId = displayedVendor.userid
                    if (vendorUserId.isNotEmpty()) {
                        Log.d("VendorProfile", "Initiating chat with vendor: $vendorUserId")
                        messageViewModel.createOrGetChat(
                            eventId = "direct_$vendorUserId",
                            eventTitle = "Chat with ${displayedVendor.name}",
                            organiserId = vendorUserId,
                            organiserName = displayedVendor.name,
                            onSuccess = { chatId ->
                                Log.d("VendorProfile", "Chat created/retrieved: $chatId")
                                onChatClick(chatId)
                            },
                            onFailure = { error ->
                                Log.e("VendorProfile", "Chat initiation failed: $error")
                                Toast.makeText(context, "Error: $error", Toast.LENGTH_SHORT).show()
                            }
                        )
                    } else {
                        Log.w("VendorProfile", "Cannot initiate chat: vendor userid is empty")
                        Toast.makeText(context, "Vendor information is not complete", Toast.LENGTH_SHORT).show()
                    }
                },
                shape = RoundedCornerShape(12.dp),
                enabled = displayedVendor.name.isNotEmpty(), // Use name instead of userid for enabling to allow interaction
                colors = ButtonDefaults.buttonColors(
                    containerColor = Purple
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("Contact Vendor", color = Color.White)
            }
        }
    }
}
