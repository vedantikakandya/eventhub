package com.example.eventhub.ui.profile

import androidx.compose.foundation.Image
import com.example.eventhub.ui.common.EventHubTopBar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.eventhub.R
import com.example.eventhub.data.model.event
import com.example.eventhub.ui.theme.Purple
import com.example.eventhub.viewmodel.eventviewmodel

@Composable
fun MyEventsScreen(
    eventViewModel: eventviewmodel = viewModel(),
    onEventClick: (String) -> Unit = {},
    onBackClick: () -> Unit = {}
) {

    val myEvents by eventViewModel.myEvents.collectAsState()

    var searchQuery by remember {
        mutableStateOf("")
    }

    LaunchedEffect(Unit) {
        eventViewModel.fetchMyEvents()
    }

    val filteredEvents = myEvents.filter {

        it.eventname.contains(
            searchQuery,
            ignoreCase = true
        )
    }
    Scaffold(


        topBar = {
            EventHubTopBar(
                title = "My Events",
                onBackClick = onBackClick,
                searchBar = {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = {
                            searchQuery = it
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = null
                            )
                        },
                        placeholder = {
                            Text("Search")
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(30.dp),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent
                        )
                    )
                }
            )
        }

    ) { innerPadding ->
        Box(
            modifier = Modifier.fillMaxSize()
        ) {

            Column(
                modifier = Modifier.fillMaxSize().padding(innerPadding)
            ) {





                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                if (filteredEvents.isEmpty()) {

                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {

                        Text(
                            text = "No Registered Events"
                        )
                    }
                } else {

                    LazyColumn(
                        contentPadding = PaddingValues(
                            horizontal = 16.dp,
                            vertical = 8.dp
                        )
                    ) {

                        items(filteredEvents) { event ->

                            MyEventCard(
                                event = event,
                                onClick = {
                                    onEventClick(event.eventId)
                                }
                            )
                        }
                    }
                }
            }

        }
    }
}

@Composable
fun MyEventCard(
    event: event,
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
                .padding(12.dp)
        ) {

            Image(
                painter = painterResource(
                    id = R.drawable.eventdetail_placeholder
                ),
                contentDescription = null,
                modifier = Modifier
                    .width(120.dp)
                    .height(90.dp)
                    .clip(
                        RoundedCornerShape(12.dp)
                    ),
                contentScale = ContentScale.Crop
            )

            Spacer(
                modifier = Modifier.width(12.dp)
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement =
                        Arrangement.SpaceBetween
                ) {

                    Text(
                        text = event.eventname,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1
                    )

                    Surface(
                        color = Purple,
                        shape = RoundedCornerShape(50)
                    ) {

                        Text(
                            text = event.category,
                            color = Color.White,
                            fontSize = 10.sp,
                            modifier = Modifier.padding(
                                horizontal = 8.dp,
                                vertical = 4.dp
                            )
                        )
                    }
                }

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                Text(
                    text = event.eventlocation,
                    color = Color.Gray,
                    fontSize = 12.sp
                )

                Spacer(
                    modifier = Modifier.height(4.dp)
                )

                Text(
                    text = event.organisername,
                    color = Color.Gray,
                    fontSize = 12.sp
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                TextButton(
                    onClick = onClick
                ) {

                    Text(
                        text = "View Details >",
                        color = Purple
                    )
                }
            }
        }
    }
}