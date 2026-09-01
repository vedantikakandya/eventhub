package com.example.eventhub.ui.event

import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import com.example.eventhub.R
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.eventhub.viewmodel.eventviewmodel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateEventScreen(
    eventId: String?,

    eventViewModel: eventviewmodel = viewModel(),

    onEditClick: (String) -> Unit = {}
){

    val selectedEvent by eventViewModel.selectedEvent.collectAsState()

    val context=LocalContext.current
    val isEditMode = !eventId.isNullOrBlank()
    var eventName by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var capacity by remember { mutableStateOf("") }

    var expanded by remember { mutableStateOf(false) }

    val categories = listOf(
        "Technology",
        "Cultural",
        "Music",
        "Sports",
        "Workshop",
        "Seminar",
        "Hackathon",
        "Business"
    )

    var category by remember { mutableStateOf("") }

    val statusOptions = listOf(
        "Open",
        "Closed"
    )

    var statusExpanded by remember {
        mutableStateOf(false)
    }

    var status by remember {
        mutableStateOf("Open")
    }

    LaunchedEffect(eventId) {

        if (isEditMode) {
            eventViewModel.fetchEventById(eventId!!)
        }
    }

    LaunchedEffect(selectedEvent) {

        if (isEditMode) {

            eventName = selectedEvent.eventname
            description = selectedEvent.eventdescription
            location = selectedEvent.eventlocation
            capacity = selectedEvent.capacity.toString()
            category = selectedEvent.category
            status = selectedEvent.status
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        Image(
            painter = painterResource(R.drawable.event_placeholder),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp),
            contentScale = ContentScale.Crop
        )

        Column (
            modifier = Modifier.padding(16.dp)
        ) {

            OutlinedTextField(
                value = eventName,
                onValueChange = {
                    eventName = it
                },
                label = {
                    Text("Event Name")
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = {
                    expanded = !expanded
                }
            ) {

                OutlinedTextField(
                    value = category,
                    onValueChange = {},
                    readOnly = true,
                    label = {
                        Text("Category")
                    },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = expanded
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = {
                        expanded = false
                    }
                ) {

                    categories.forEach {

                        DropdownMenuItem(
                            text = {
                                Text(it)
                            },
                            onClick = {
                                category = it
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = location,
                onValueChange = {
                    location = it
                },
                label = {
                    Text("Location")
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = description,
                onValueChange = {
                    description = it
                },
                label = {
                    Text("Description")
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = capacity,
                onValueChange = {
                    capacity = it
                },
                label = {
                    Text("Capacity")
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            ExposedDropdownMenuBox(
                expanded = statusExpanded,
                onExpandedChange = {
                    statusExpanded = !statusExpanded
                }
            ) {

                OutlinedTextField(
                    value = status,
                    onValueChange = {},
                    readOnly = true,
                    label = {
                        Text("Status")
                    },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = statusExpanded
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = statusExpanded,
                    onDismissRequest = {
                        statusExpanded = false
                    }
                ) {

                    statusOptions.forEach {

                        DropdownMenuItem(
                            text = {
                                Text(it)
                            },
                            onClick = {
                                status = it
                                statusExpanded = false
                            }
                        )
                    }
                }
            }


            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {

                    if (isEditMode) {
                        eventViewModel.updateEvent(
                            eventId = eventId!!,
                            eventName = eventName,
                            description = description,
                            location = location,
                            category = category,
                            capacity = capacity.toIntOrNull() ?: 0,
                            status = status,
                            onSuccess = {
                                Toast.makeText(context, "Event Updated", Toast.LENGTH_SHORT).show()
                            }
                        )


                    } else {

                        eventViewModel.createEvent(
                            eventName = eventName,
                            description = description,
                            location = location,
                            category = category,
                            capacity = capacity.toIntOrNull() ?: 0,
                            status = status,
                            onSuccess = {
                                Toast.makeText(context, "Event Created", Toast.LENGTH_SHORT).show()
                            }
                        )

                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {

                Text(
                    if (isEditMode)
                        "Update Event"
                    else
                        "Create Event"
                )
            }

        }
    }
}
