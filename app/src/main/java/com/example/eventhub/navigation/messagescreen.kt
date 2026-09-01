package com.example.eventhub.ui.messages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.eventhub.viewmodel.MessageViewModel

@Composable
fun MessagesScreen(
    onChatClick: (String) -> Unit,
    viewModel: MessageViewModel = viewModel()
) {

    val chats by viewModel.chats.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchChats()
    }

    if (chats.isEmpty()) {

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {

            Text(
                text = "No messages yet"
            )
        }

    } else {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),

            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            chats.forEach { chat ->

                Text(
                    text = chat.eventTitle,

                    modifier = Modifier
                        .padding(16.dp)
                )
            }
        }
    }
}