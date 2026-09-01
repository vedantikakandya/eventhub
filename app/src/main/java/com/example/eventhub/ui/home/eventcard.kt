package com.example.eventhub.ui.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.eventhub.R
import com.example.eventhub.data.model.event
import com.example.eventhub.data.model.vendor

@Composable
fun EventCard(
    event: event,
    onClick: () -> Unit = {}
) {

    Card(
        modifier = Modifier
            .fillMaxWidth().padding(start=8.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.event_placeholder),
            contentDescription = event.eventname,
            modifier = Modifier
                .fillMaxWidth()
                .height(149.dp),
            contentScale = ContentScale.Crop
        )

            Column(
                modifier = Modifier.padding(12.dp).background(Color.Transparent)

            ) {
                Box(
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(50)
                        )
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = event.category,
                        style = MaterialTheme.typography.labelSmall, // Using themed label style
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Event Name
                Text(
                    text = event.eventname,
                    style = MaterialTheme.typography.bodyLarge, // Using themed body style
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Location
                Text(
                    text = "📍 ${event.eventlocation}",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(2.dp))

                // Organiser
                Text(
                    text = "By ${event.organisername}",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.DarkGray
                )

                Spacer(modifier = Modifier.height(2.dp))

                // Registration Info
                Text(
                    text = "Registered: ${event.registeredcount}/${event.capacity}",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Status Indicator
                val statusColor = when (event.status.lowercase()) {
                    "open" -> Color(0xFF2E7D32)
                    "closed" -> Color.Red
                    else -> Color.Gray
                }

                Text(
                    text = event.status.uppercase(),
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = statusColor
                )
            }
        }
    }

@Composable
fun vendorcard(
    vendor: vendor,
    onClick: () -> Unit = {}
){
    Card( modifier = Modifier
        .fillMaxWidth().padding(start=8.dp).height(165.dp).width(152.dp)
        .clickable { onClick() },
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ){
        Box(modifier=Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = R.drawable.vendor_placeholder),
                contentDescription = vendor.name,
                modifier = Modifier.matchParentSize(),
                contentScale = ContentScale.Crop
            )
            Box(
                modifier=Modifier.matchParentSize()
                    .background(Color.Black.copy(alpha = 0.5f))
//                colorFilter = ColorFilter.tint(
//                color = Color.Black.copy(alpha = 0.5f),
//                blendMode = BlendMode.Darken
//            )
            ){
            Column(
                modifier = Modifier.padding(12.dp)
                    .background(Color.Transparent),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.vendorimage_placeholder),
                    contentDescription = vendor.name,
                    modifier = Modifier.size(40.dp).clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = vendor.name,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "${vendor.eventshosted} Events hosted",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.LightGray
                )
                Spacer(modifier = Modifier.height(26.dp))

                OutlinedButton(
                    onClick = {},
                    shape = RoundedCornerShape(12),
                    modifier = Modifier.fillMaxWidth().height(30.dp),
                    border = BorderStroke(width = 1.dp, color = Color.White)
                ) {
                    Text(
                        text = "View Profile >",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White
                    )
                }
            }
            }
        }
    }
}
