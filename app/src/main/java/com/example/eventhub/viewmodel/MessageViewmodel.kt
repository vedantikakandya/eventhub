package com.example.eventhub.viewmodel

import androidx.lifecycle.ViewModel
import com.example.eventhub.data.model.chat
import com.example.eventhub.data.model.message
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.google.firebase.database.ValueEventListener

class MessageViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()

    private val database =
        FirebaseDatabase.getInstance()

    private val _chats =
        MutableStateFlow<List<chat>>(emptyList())

    val chats: StateFlow<List<chat>>
            = _chats
    private val _messages =
        MutableStateFlow<List<message>>(emptyList())

    val messages: StateFlow<List<message>>
            = _messages

    private val chatsRef =
        database.getReference("chats")

    private val messagesRef =
        database.getReference("messages")


    private val userChatsRef =
        database.getReference("userChats")



    fun createOrGetChat(
        eventId: String,
        eventTitle: String,
        organiserId: String,
        organiserName: String,
        onSuccess: (String) -> Unit,
        onFailure: (String) -> Unit = {}
    ) {

        val attendeeId = auth.currentUser?.uid

        if (attendeeId == null) {
            onFailure("User not logged in")
            return
        }

        val chatId = "${eventId}_${attendeeId}"

        chatsRef.child(chatId)
            .get()
            .addOnSuccessListener { snapshot ->

                if (snapshot.exists()) {

                    onSuccess(chatId)

                } else {

                    val chat = chat(
                        chatId = chatId,
                        eventId = eventId,
                        eventTitle = eventTitle,
                        attendeeId = attendeeId,
                        organiserId = organiserId,
                        organiserName = organiserName,
                        lastMessage = "",
                        lastMessageTime = System.currentTimeMillis()
                    )

                    chatsRef.child(chatId)
                        .setValue(chat)
                        .addOnSuccessListener {

                            userChatsRef
                                .child(attendeeId)
                                .child(chatId)
                                .setValue(true)

                            userChatsRef
                                .child(organiserId)
                                .child(chatId)
                                .setValue(true)

                            onSuccess(chatId)

                        }
                        .addOnFailureListener {

                            onFailure(
                                it.message ?: "Failed to create chat"
                            )

                        }
                }

            }
            .addOnFailureListener {

                onFailure(it.message ?: "Something went wrong")

            }

    }


    fun sendMessage(
        chatId: String,
        text: String,
        onFailure: (String) -> Unit = {}
    ) {

        if (text.isBlank()) return

        val senderId = auth.currentUser?.uid ?: return

        val messageRef = messagesRef.child(chatId).push()

        val messageId = messageRef.key ?: return

        val message = message(
            messageId = messageId,
            senderId = senderId,
            text = text.trim(),
            timestamp = System.currentTimeMillis()
        )

        messageRef
            .setValue(message)
            .addOnSuccessListener {

                chatsRef.child(chatId)
                    .child("lastMessage")
                    .setValue(text.trim())

                chatsRef.child(chatId)
                    .child("lastMessageTime")
                    .setValue(System.currentTimeMillis())

            }
            .addOnFailureListener {

                onFailure(it.message ?: "Failed to send message")

            }
    }


    fun fetchChats() {

        val currentUserId = auth.currentUser?.uid

        if (currentUserId == null) {
            _chats.value = emptyList()
            return
        }

        userChatsRef
            .child(currentUserId)
            .addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {

                    if (!snapshot.exists()) {
                        _chats.value = emptyList()
                        return
                    }

                    val chatIds = snapshot.children
                        .mapNotNull { it.key }

                    val chatList = mutableListOf<chat>()

                    chatIds.forEach { chatId ->

                        chatsRef
                            .child(chatId)
                            .get()
                            .addOnSuccessListener { chatSnapshot ->

                                val chat =
                                    chatSnapshot.getValue(chat::class.java)

                                if (chat != null) {

                                    chatList.removeAll {
                                        it.chatId == chat.chatId
                                    }

                                    chatList.add(chat)

                                    chatList.sortByDescending {
                                        it.lastMessageTime
                                    }

                                    _chats.value = chatList
                                }
                            }
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                    _chats.value = emptyList()
                }
            })
    }


    fun listenForMessages(chatId: String) {

        messagesRef.child(chatId)
            .addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {

                    val messageList = mutableListOf<message>()

                    snapshot.children.forEach {

                        val message =
                            it.getValue(message::class.java)

                        if (message != null)
                            messageList.add(message)

                    }

                    messageList.sortBy { it.timestamp }

                    _messages.value = messageList

                }

                override fun onCancelled(error: DatabaseError) {

                }

            }
            )

    }
}