package com.example.eventhub.data.model

import com.google.firebase.Timestamp

data class event(
    val eventId: String="",
    val eventname: String = "",
    val eventdescription: String = "",
    val capacity: Int=0,
    val category: String = "",
    val endtime: Timestamp? = null,
    val eventlocation: String="",
    val organiserid:String="",
    val organisername: String="",
    val posterurl:String="",
    val registeredcount:Int=0,
    val starttime: Timestamp? = null,
    val status: String=""
)
