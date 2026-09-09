package com.example.eventhub.ui.home

import androidx.compose.foundation.layout.Arrangement
import com.example.eventhub.ui.common.EventHubTopBar
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.eventhub.viewmodel.eventviewmodel
import com.example.eventhub.viewmodel.vendorviewmodel

@Composable
fun homescreen(
    onEventClick: (String) -> Unit,
    onVendorClick: (String) -> Unit,
    onBackClick: () -> Unit = {},
    eventViewModel: eventviewmodel = viewModel(),
    vendorViewModel: vendorviewmodel = viewModel()
){
    val events = eventViewModel.events.collectAsState()
    val vendors = vendorViewModel.vendors.collectAsState()

    LaunchedEffect(Unit) {
        eventViewModel.fetchevents()
        vendorViewModel.fetchvendors()
    }
    Scaffold(

        topBar = {

            EventHubTopBar(

                title = "Home",

                onBackClick = onBackClick
            )
        }

    ) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(innerPadding).padding(16.dp)
        ) {

            Text(
                text = "Explore Events",
                style = MaterialTheme.typography.titleLarge // Applied from Theme.kt
            )
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                LazyRow() {
                    items(events.value) { eventItem ->
                        EventCard(event = eventItem, onClick = { onEventClick(eventItem.eventId) })
                    }
                }

            }

            Spacer(modifier = Modifier.height(22.dp))
            Text(
                text = "Explore Vendors",
                style = MaterialTheme.typography.titleLarge // Applied from Theme.kt
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                LazyRow() {
                    items(vendors.value) { vendoritem ->
                        vendorcard(
                            vendor = vendoritem,
                            onClick = { onVendorClick(vendoritem.vendorId) })
                    }
                }

            }
        }
    }
}
