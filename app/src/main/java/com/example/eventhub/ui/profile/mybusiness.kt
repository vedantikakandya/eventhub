package com.example.eventhub.ui.profile


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.eventhub.R
import com.example.eventhub.data.model.event
import com.example.eventhub.data.model.vendor
import com.example.eventhub.viewmodel.eventviewmodel
import com.example.eventhub.viewmodel.vendorviewmodel

@Composable
fun MyBusinessScreen(

    onEditEventClick: (String) -> Unit,
    onCreateEventClick: () -> Unit,
    vendorviewmodel: vendorviewmodel=viewModel(),
    eventViewModel: eventviewmodel = viewModel()

) {

    val events by
    eventViewModel.myHostedEvents.collectAsState()

    var searchQuery by remember {
        mutableStateOf("")
    }

    LaunchedEffect(Unit) {
        eventViewModel.fetchMyHostedEvents()
    }

    val filteredEvents =
        events.filter {

            it.eventname.contains(
                searchQuery,
                ignoreCase = true
            )
        }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            OutlinedTextField(
                value = searchQuery,
                onValueChange = {
                    searchQuery = it
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                leadingIcon = {
                    Icon(
                        imageVector =
                            Icons.Default.Search,
                        contentDescription = null
                    )
                },
                placeholder = {
                    Text("Search Events")
                }
            )

            LazyColumn {

                items(filteredEvents) { event ->

                    EventCard(
                        event = event,
                        onClick = {

                            onEditEventClick(
                                event.eventId
                            )
                        }
                    )
                }
            }
        }

        FloatingActionButton(
            onClick = {
                onCreateEventClick()
            },
            modifier = Modifier
                .align(
                    Alignment.BottomEnd
                )
                .padding(20.dp)
        ) {
            Text("+")
        }
    }
}

@Composable
fun EventCard(

    event: event,
    onClick: () -> Unit

) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 16.dp,
                vertical = 8.dp
            ),
        elevation =
            CardDefaults.cardElevation(
                defaultElevation = 4.dp
            )
    ) {

        Column(
            modifier =
                Modifier.padding(16.dp)
        ) {

            Text(
                text = event.eventname
            )

            Spacer(
                modifier =
                    Modifier.height(6.dp)
            )

            Text(
                text = event.category
            )

            Spacer(
                modifier =
                    Modifier.height(6.dp)
            )

            Text(
                text = event.eventlocation
            )

            Spacer(
                modifier =
                    Modifier.height(8.dp)
            )

            TextButton(
                onClick = onClick
            ) {

                Text("Edit Event")
            }
        }
    }
}

@Composable
fun BusinessCard(

    vendor: vendor,
    onViewDetails:()->Unit

){

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 16.dp,
                vertical = 8.dp
            ),

        elevation =
            CardDefaults.cardElevation(
                defaultElevation = 4.dp
            )
    ) {

        Row(
            modifier = Modifier.padding(12.dp)
        ) {

            Image(
                painter = painterResource(
                    id = R.drawable.vendor_placeholder
                ),
                contentDescription = null,
                modifier = Modifier
                    .size(100.dp),

                contentScale =
                    ContentScale.Crop
            )

            Spacer(
                modifier = Modifier.width(12.dp)
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = vendor.name
                )

                Spacer(
                    modifier = Modifier.height(6.dp)
                )

                Text(
                    text = "⭐ ${vendor.rating}"
                )

                Spacer(
                    modifier = Modifier.height(6.dp)
                )

                Text(
                    text = vendor.address
                )

                Spacer(
                    modifier = Modifier.height(6.dp)
                )

                Text(
                    text = vendor.number.toString()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement =
                        Arrangement.End
                ) {

                    TextButton(
                        onClick = onViewDetails
                    ) {
                        Text(
                            "View Details"
                        )
                    }
                }
            }
        }
    }
}