package com.example.eventhub.data.model

data class chat (
    val chatId: String = "",

    val eventId: String = "",
    val eventTitle: String = "",

    val attendeeId: String = "",

    val organiserId: String = "",
    val organiserName: String = "",

    val lastMessage: String = "",

    val lastMessageTime: Long =  0L
)