package com.example.eventhub.data.model

data class vendor(
    val vendorId: String = "",
    val userid:String="",
    val name: String = "",
    val address:String="",
    val number: Long = 0, // Changed from Int to Long to handle phone numbers
    val logourl: String = "",
    val description: String = "",
    val email: String = "",
    val eventshosted: Int = 0,
    val category: String="",
    val rating: Double = 0.0
)
