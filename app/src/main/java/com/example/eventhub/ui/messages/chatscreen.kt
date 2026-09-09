package com.example.eventhub.ui.messages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.eventhub.data.model.message
import com.example.eventhub.ui.common.EventHubTopBar
import com.example.eventhub.ui.theme.Purple
import com.example.eventhub.ui.theme.Purplelight
import com.example.eventhub.viewmodel.MessageViewModel
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    chatId: String,
    onBackClick: () -> Unit = {},
    viewModel: MessageViewModel = viewModel()
) {

    val messages by viewModel.messages.collectAsState()

    var messageText by remember {
        mutableStateOf("")
    }

    val currentUserId =
        FirebaseAuth.getInstance().currentUser?.uid

    val listState = rememberLazyListState()

    LaunchedEffect(chatId) {
        viewModel.listenForMessages(chatId)
    }

    LaunchedEffect(messages.size) {

        if (messages.isNotEmpty()) {

            listState.animateScrollToItem(
                messages.lastIndex
            )
        }
    }

    Scaffold(

        topBar = {

            EventHubTopBar(

                title = "Chat",

                onBackClick = onBackClick
            )
        }

    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            LazyColumn(

                state = listState,

                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .background(Color.White),

                contentPadding = androidx.compose.foundation.layout.PaddingValues(
                    horizontal = 12.dp,
                    vertical = 12.dp
                ),

                verticalArrangement =
                    Arrangement.spacedBy(6.dp)
            ) {

                items(
                    items = messages,
                    key = {
                        it.messageId
                    }
                ) { currentMessage ->

                    MessageBubble(
                        message = currentMessage,
                        isCurrentUser =
                            currentMessage.senderId == currentUserId
                    )
                }
            }

            ChatInput(
                text = messageText,

                onTextChange = {
                    messageText = it
                },

                onSendClick = {

                    if (messageText.isNotBlank()) {

                        viewModel.sendMessage(
                            chatId = chatId,
                            text = messageText
                        )

                        messageText = ""
                    }
                }
            )
        }
    }
}


@Composable
private fun MessageBubble(
    message: message,
    isCurrentUser: Boolean
) {

    Row(

        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),

        horizontalArrangement =
            if (isCurrentUser)
                Arrangement.End
            else
                Arrangement.Start
    ) {

        Surface(

            modifier = Modifier
                .widthIn(max = 280.dp),

            shape = RoundedCornerShape(
                topStart = 18.dp,
                topEnd = 18.dp,
                bottomStart =
                    if (isCurrentUser) 18.dp else 4.dp,
                bottomEnd =
                    if (isCurrentUser) 4.dp else 18.dp
            ),

            color =
                if (isCurrentUser)
                    Purplelight
                else
                    Color(0xFFF4F4F4)
        ) {

            Column(
                modifier = Modifier.padding(
                    horizontal = 14.dp,
                    vertical = 10.dp
                )
            ) {

                Text(
                    text = message.text,

                    style =
                        MaterialTheme.typography.bodyMedium,
                    color = Purple
                )

                Spacer(
                    modifier = Modifier.height(4.dp)
                )

                Text(
                    text = formatMessageTime(
                        message.timestamp
                    ),

                    style =
                        MaterialTheme.typography.labelSmall,

                    color = Color.Gray,

                    modifier =
                        Modifier.align(Alignment.End)
                )
            }
        }
    }
}


@Composable
private fun ChatInput(
    text: String,
    onTextChange: (String) -> Unit,
    onSendClick: () -> Unit
) {

    Row(

        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 12.dp,
                vertical = 8.dp
            ),

        verticalAlignment =
            Alignment.CenterVertically
    ) {

        TextField(

            value = text,

            onValueChange = onTextChange,

            modifier = Modifier
                .weight(1f),

            placeholder = {
                Text("Type a message...")
            },

            singleLine = true,

            shape = RoundedCornerShape(24.dp)
        )

        IconButton(

            onClick = onSendClick,

            enabled = text.isNotBlank(),

            modifier = Modifier
                .padding(start = 6.dp)
        ) {

            Icon(
                imageVector = Icons.Default.Send,

                contentDescription = "Send",

                tint = Purple
            )
        }
    }
}


private fun formatMessageTime(
    timestamp: Long
): String {

    if (timestamp == 0L) {
        return ""
    }

    val formatter =
        SimpleDateFormat(
            "hh:mm a",
            Locale.getDefault()
        )

    return formatter.format(
        Date(timestamp)
    )
}