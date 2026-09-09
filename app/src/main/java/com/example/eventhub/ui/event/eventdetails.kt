package com.example.eventhub.ui.event

import android.widget.Toast
import com.example.eventhub.ui.common.EventHubTopBar
import com.example.eventhub.viewmodel.MessageViewModel
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.eventhub.R
import com.example.eventhub.data.model.event
import com.example.eventhub.viewmodel.eventviewmodel
import com.google.firebase.Timestamp

@Composable
fun eventdetails(
    eventId: String,
    initialEvent: event? = null,
    eventViewModel: eventviewmodel = viewModel(),
    messageViewModel: MessageViewModel = viewModel(),
    onChatClick: (String) -> Unit = {},
    onBackClick: () -> Unit = {},
    onregisterclick: () -> Unit = {}
) {
    val context = LocalContext.current
    var isRegistered by remember {
        mutableStateOf(false)
    }
    val selectedEvent by eventViewModel.selectedEvent.collectAsState()
    
    // Use the provided initialEvent or the one from the ViewModel
    val displayEvent = initialEvent ?: selectedEvent

    LaunchedEffect(eventId) {
        if (initialEvent == null) {
            eventViewModel.fetchEventById(eventId)
        }
    }


        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            Image(
                painter = painterResource(id = R.drawable.eventdetail_placeholder),
                contentDescription = "event image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = displayEvent.eventname,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = displayEvent.category,
                        fontSize = 8.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = displayEvent.status,
                        fontSize = 8.sp,
                        color = Color.Gray
                    )

                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "About Event",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = displayEvent.eventdescription,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Location",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                )
                Text(
                    text = displayEvent.eventlocation,
                    fontSize = 12.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Organiser",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                )
                Text(
                    text = displayEvent.organisername,
                    fontSize = 12.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Capacity",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                )
                Text(
                    text = "${displayEvent.registeredcount}/${displayEvent.capacity}",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = {
                        eventViewModel.registerForEvent(
                            eventId = displayEvent.eventId,
                            eventTitle = displayEvent.eventname,
                            onSuccess = {
                                eventViewModel.fetchEventById(eventId)
                                isRegistered = true
                                Toast.makeText(
                                    context,
                                    "Registered Successfully",
                                    Toast.LENGTH_SHORT
                                ).show()
                            },
                            onFailure = { error ->
                                Toast.makeText(context, error, Toast.LENGTH_SHORT).show()

                            }
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        if (isRegistered)
                            "Registered!!!!"
                        else
                            "Register"

                    ,color = Color.White)

                }
                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {

                        messageViewModel.createOrGetChat(
                            eventId = displayEvent.eventId,
                            eventTitle = displayEvent.eventname,
                            organiserId = displayEvent.organiserid,
                            organiserName = displayEvent.organisername,

                            onSuccess = { chatId ->

                                onChatClick(chatId)

                            },

                            onFailure = { error ->

                                Toast.makeText(
                                    context,
                                    error,
                                    Toast.LENGTH_SHORT
                                ).show()

                            }
                        )
                    },

                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),

                    shape = RoundedCornerShape(12.dp)
                ) {

                    Text("Contact Organiser",color = Color.White)
                }
            }
        }

}


@Composable
@Preview(showBackground = true)
fun PreviewEventDetail() {

    val sampleEvent = event(
        eventname = "Tech Fest 2026",
        eventdescription = "This is a sample event description for preview.",
        capacity = 200,
        category = "Technology",
        endtime = Timestamp.now(),
        eventlocation = "Main Hall",
        organisername = "E-Cell",
        registeredcount = 120,
        starttime = Timestamp.now(),
        status = "Open"
    )

    eventdetails(eventId = "1", initialEvent = sampleEvent)
}
