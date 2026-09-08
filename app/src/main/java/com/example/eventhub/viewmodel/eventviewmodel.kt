package com.example.eventhub.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.eventhub.data.model.event
import com.example.eventhub.data.model.registrationonevent
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.google.firebase.Timestamp
import kotlinx.coroutines.flow.combine

class eventviewmodel: ViewModel(){

    private val _searchQuery =
        MutableStateFlow("")

    val searchQuery: StateFlow<String> =
        _searchQuery
    private val _myEvents =
        MutableStateFlow<List<event>>(emptyList())

    private val _myHostedEvents =
        MutableStateFlow<List<event>>(emptyList())

    val myHostedEvents: StateFlow<List<event>>
            = _myHostedEvents
    val myEvents: StateFlow<List<event>> =
        _myEvents
    private val firestore = FirebaseFirestore.getInstance()
    private val _events = MutableStateFlow<List<event>>(emptyList())
    val events: StateFlow<List<event>> = _events
    private val _selectedEvent =
        MutableStateFlow(event())

    val selectedEvent: StateFlow<event> =
        _selectedEvent


    fun fetchMyHostedEvents() {

        val uid =
            FirebaseAuth.getInstance()
                .currentUser?.uid ?: return

        firestore.collection("events")
            .whereEqualTo(
                "organiserid",
                uid
            )
            .get()
            .addOnSuccessListener { result ->

                val list =
                    result.documents.mapNotNull {

                        val event =
                            it.toObject(event::class.java)

                        event?.copy(
                            eventId = it.id
                        )
                    }

                _myHostedEvents.value = list
            }
    }

    fun fetchMyEvents() {

        val uid =
            FirebaseAuth.getInstance()
                .currentUser?.uid ?: return

        firestore.collection("registrations")
            .whereEqualTo("userid", uid)
            .get()
            .addOnSuccessListener { registrationDocs ->

                val eventIds =
                    registrationDocs.documents.mapNotNull {
                        it.getString("eventid")
                    }

                if(eventIds.isEmpty()){
                    _myEvents.value = emptyList()
                    return@addOnSuccessListener
                }

                firestore.collection("events")
                    .get()
                    .addOnSuccessListener { eventDocs ->

                        val registeredEvents =
                            eventDocs.documents.mapNotNull {

                                val event =
                                    it.toObject(event::class.java)

                                event?.copy(
                                    eventId = it.id
                                )
                            }.filter {

                                eventIds.contains(it.eventId)
                            }

                        _myEvents.value =
                            registeredEvents
                    }
            }
    }
    fun updateSearchQuery(query: String) {
        _searchQuery.value = query

    }

        val filteredEvents =
            combine(
                events,
                searchQuery
            ) { events, query ->

                if (query.isBlank()) {

                    events

                } else {

                    events.filter {

                        it.eventname.contains(
                            query,
                            ignoreCase = true
                        )

                                ||

                                it.eventlocation.contains(
                                    query,
                                    ignoreCase = true
                                )

                                ||

                                it.category.contains(
                                    query,
                                    ignoreCase = true
                                )

                                ||

                                it.organisername.contains(
                                    query,
                                    ignoreCase = true
                                )

                    }
                }
            }

    fun updateEvent(
        eventId: String,
        eventName: String,
        description: String,
        location: String,
        category: String,
        capacity: Int,
        status: String,
        onSuccess: () -> Unit
    ) {

        firestore.collection("events")
            .document(eventId)
            .update(
                mapOf(
                    "eventname" to eventName,
                    "eventdescription" to description,
                    "eventlocation" to location,
                    "category" to category,
                    "capacity" to capacity,
                    "status" to status
                )
            )
            .addOnSuccessListener {

                fetchEventById(eventId)
                fetchevents()

                onSuccess()
            }
            .addOnFailureListener {
                Log.e("EVENT", "Update Failed", it)
            }
    }

    fun createEvent(
        eventName: String,
        description: String,
        location: String,
        category: String,
        capacity: Int,
        status: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {

        val authUser =
            FirebaseAuth.getInstance().currentUser

        if (authUser == null) {

            onFailure("User is not logged in")
            return
        }

        val docRef = firestore.collection("events").document()
        firestore.collection("users")
            .document(authUser.uid)
            .get()
            .addOnSuccessListener { userDoc ->

                val organiserName =
                    userDoc.getString("name") ?: ""

                val newEvent = event(
                    eventId=docRef.id,
                    eventname = eventName,
                    eventdescription = description,
                    eventlocation = location,
                    category = category,
                    capacity = capacity,
                    status = status,
                    organiserid = authUser.uid,
                    organisername = organiserName,
                    registeredcount = 0,
                    starttime = Timestamp.now(),
                    endtime = Timestamp.now()
                )

                docRef.set(newEvent)
                    .addOnSuccessListener {
                        fetchevents()
                        onSuccess()
                    }
                    .addOnFailureListener {
                        Log.e("EVENT", "Create Failed", it)

                        onFailure(
                            it.message
                                ?: "Failed to create event"
                        )
                    }
            }
            .addOnFailureListener {
                Log.e("USER", "Fetch Failed", it)
            }
    }
    fun registerForEvent(
        eventId: String,
        eventTitle: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ){
        val authUser =
            FirebaseAuth.getInstance().currentUser

        if (authUser == null) {

            onFailure("User is not logged in")
            return
        }

        firestore.collection("users")
            .document(authUser.uid)
            .get()
            .addOnSuccessListener{userDoc ->

                val userName =
                    userDoc.getString("name") ?: "Unknown User"

                val registration = registrationonevent(
                    eventid = eventId,
                    eventtitle = eventTitle,
                    userid = authUser.uid,
                    username = userName,
                    registrationdate = Timestamp.now()
                )

                firestore.collection("registrations")
                    .add(registration)
                    .addOnSuccessListener {

                        firestore.collection("events")
                            .document(eventId)
                            .update(
                                "registeredcount",
                                com.google.firebase.firestore.FieldValue.increment(1)
                            ).addOnSuccessListener {
                                onSuccess()

                            }
                            .addOnFailureListener {
                                onFailure(
                                    it.message?:"Failed to update register count"
                                )
                                Log.e("REGISTER", "Failed", it)
                            }

                    }
                    .addOnFailureListener {
                        onFailure(it.message ?: "Failed to register")
                    }
            }
            .addOnFailureListener {
                onFailure(
                    it.message?:"failed to get user information"
                )
            }

    }
    fun fetchEventById(eventId: String) {

        firestore.collection("events")
            .document(eventId)
            .get()
            .addOnSuccessListener { document ->

                val eventData =
                    document.toObject(event::class.java)

                if(eventData != null) {

                    _selectedEvent.value =
                        eventData.copy(
                            eventId = document.id
                        )
                }
            }
    }
    fun fetchevents() {
        firestore.collection("events").get()
            .addOnSuccessListener { result ->
                val eventlist = mutableListOf<event>()
                for (document in result) {
                    // document.toObject can return null if mapping fails
                    val eventitem = document.toObject(event::class.java)
                    eventitem?.let {
                        val eventwithid = it.copy(eventId = document.id)
                        eventlist.add(eventwithid)
                    }
                }
                _events.value = eventlist
            }
            .addOnFailureListener {
                // Log error or handle failure
            }
    }
}
