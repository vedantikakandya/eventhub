package com.example.eventhub.viewmodel

import androidx.lifecycle.ViewModel
import com.example.eventhub.data.model.vendor
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class vendorviewmodel : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()
    private val _selectedVendor =
        MutableStateFlow(vendor())

    private val _myBusinesses =
        MutableStateFlow<List<vendor>>(emptyList())

    val myBusinesses: StateFlow<List<vendor>>
            = _myBusinesses
    val selectedVendor: StateFlow<vendor> =
        _selectedVendor
    private val _vendors = MutableStateFlow<List<vendor>>(emptyList())
    val vendors: StateFlow<List<vendor>> = _vendors

    private val _isVendor =
        MutableStateFlow<Boolean?>(null)

    val isVendor: StateFlow<Boolean?> =
        _isVendor


    fun checkIfVendor(
        onResult: (Boolean) -> Unit
    ) {

        val uid =
            FirebaseAuth.getInstance()
                .currentUser?.uid

        if (uid == null) {

            onResult(false)

            return
        }

        firestore.collection("vendor")
            .whereEqualTo(
                "userid",
                uid
            )
            .get()
            .addOnSuccessListener { result ->

                onResult(
                    !result.isEmpty
                )
            }
            .addOnFailureListener {

                onResult(false)
            }
    }
    fun fetchMyBusinesses(){

        val uid =
            FirebaseAuth.getInstance()
                .currentUser?.uid
                ?: return

        firestore.collection("vendor")
            .whereEqualTo(
                "userid",
                uid
            )
            .get()
            .addOnSuccessListener { result ->

                val list =
                    result.documents.mapNotNull {

                        it.toObject(
                            vendor::class.java
                        )
                    }

                _myBusinesses.value =
                    list
            }
    }
    fun createVendor(
        vendor: vendor,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {

        firestore.collection("vendor")
            .add(vendor)
            .addOnSuccessListener { document ->

                document.update(
                    "vendorId",
                    document.id
                )

                firestore.collection("users")
                    .document(vendor.userid)
                    .update(
                        "role",
                        "organiser"
                    )
                    .addOnSuccessListener {
                        onSuccess()
                    }
                    .addOnFailureListener { e ->
                        onFailure(e.message ?: "Failed to update user role")
                    }
            }
            .addOnFailureListener { e ->
                onFailure(e.message ?: "Failed to create vendor")
            }
    }
    fun fetchvendors() {
        firestore.collection("vendor")
            .get()
            .addOnSuccessListener { result ->
                val vendorList = mutableListOf<vendor>()
                for (document in result) {
                    val vendorItem = document.toObject(vendor::class.java)
                    vendorItem?.let {
                        // Ensure the vendorId is set from the document ID if needed
                        val vendorWithId = it.copy(vendorId = document.id)
                        vendorList.add(vendorWithId)
                    }
                }
                _vendors.value = vendorList
            }
            .addOnFailureListener {
                // Handle failure
            }
    }

    fun fetchVendorById(vendorId: String) {

        firestore.collection("vendor")
            .document(vendorId)
            .get()
            .addOnSuccessListener { document ->

                val vendorData =
                    document.toObject(vendor::class.java)

                if(vendorData != null){

                    _selectedVendor.value =
                        vendorData.copy(
                            vendorId = document.id
                        )
                }
            }
    }
}
