package com.example.eventhub.navigation

import androidx.compose.foundation.layout.Column
import com.example.eventhub.ui.common.EventHubTopBar
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.eventhub.ui.home.EventCard
import com.example.eventhub.viewmodel.eventviewmodel


@Composable
fun ExploreScreen(
    eventViewModel: eventviewmodel = viewModel(),
    onBackClick: () -> Unit = {},
    onEventClick: (String) -> Unit = {}
) {
    val query by eventViewModel.searchQuery.collectAsState()
    val filteredEvents by eventViewModel.filteredEvents.collectAsState(
        initial = emptyList()
    )

    LaunchedEffect(Unit) {
        eventViewModel.fetchevents()
    }

    Scaffold(
        topBar = {
            EventHubTopBar(
                title = "Explore",
                onBackClick = onBackClick,
                searchBar = {
                    OutlinedTextField(
                        value = query,
                        onValueChange = {
                            eventViewModel.updateSearchQuery(it)
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = null
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = {
                            Text("Search Events")
                        },
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Text(
                text = "Found ${filteredEvents.size} Events",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            LazyColumn {
                items(filteredEvents.size) { index ->
                    val event = filteredEvents[index]
                    EventCard(
                        event = event,
                        onClick = { onEventClick(event.eventId) }
                    )
                    Spacer(
                        modifier = Modifier.height(16.dp)
                    )
                }
            }
        }
    }
}