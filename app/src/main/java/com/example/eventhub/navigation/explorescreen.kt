package com.example.eventhub.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.eventhub.ui.home.EventCard
import com.example.eventhub.viewmodel.eventviewmodel


@Composable
fun ExploreScreen(
    eventViewModel: eventviewmodel = viewModel()

) {

    val query by eventViewModel.searchQuery.collectAsState()

    val filteredEvents by eventViewModel.filteredEvents.collectAsState (
        initial = emptyList()
    )
    LaunchedEffect(Unit) {
        eventViewModel.fetchevents()
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Explore Events",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        OutlinedTextField(
            value = query,
            onValueChange = {
                eventViewModel.updateSearchQuery(it)
            },
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text("Search Events")
            },
            singleLine = true
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        Text(
            text = "Found ${filteredEvents.size} Events"
        )


        Spacer(
            modifier = Modifier.height(16.dp)
        )

        LazyColumn() {
            items(filteredEvents.size) { event ->

                EventCard(event = filteredEvents[event])
                Spacer(
                    modifier = Modifier.height(16.dp)
                )
            }
        }
    }
}