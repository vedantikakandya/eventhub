package com.example.eventhub.data.model

data class message (
    val messageId: String = "",

    val senderId: String = "",

    val text: String = "",

    val timestamp: Long = 0L
)