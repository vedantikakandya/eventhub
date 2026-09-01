package com.example.eventhub.data.model

import com.google.firebase.Timestamp

data class registrationonevent(
    val eventid:String="",
    val eventtitle:String="",
    val registrationdate: Timestamp?=null,
    val userid:String="",
    val username:String="",
)