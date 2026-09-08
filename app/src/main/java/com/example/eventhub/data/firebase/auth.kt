package com.example.eventhub.data.firebase

import android.util.Log
import com.example.eventhub.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class authfirebase() {
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    fun siginupfrebase(
        user: User,
        password: String,
        onResult: (Boolean, String?) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(user.email, password)
            .addOnSuccessListener { result ->
                val uid = result.user?.uid
                uid?.let {
                    firestore.collection("users").document(it).set(user)
                        .addOnSuccessListener {
                            onResult(true, null)
                            Log.d("Signup", "User saved in Firestore")

                        }
                        .addOnFailureListener { e ->
                            onResult(false, e.message ?: "Firestore error occurred")
                            Log.e("Signup", "Firestore error: ${e.message}")

                        }
                }

            }
            .addOnFailureListener { e ->
                onResult(false, e.message ?: "Signup failed")
            }
    }
    fun loginfirebase(
        email: String,
        password: String,
        onResult: (Boolean, String?) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                onResult(true, null)
            }
            .addOnFailureListener { e ->
                onResult(false, e.message ?: "Login failed")
            }
    }

    fun isuserlogged():Boolean{
        return auth.currentUser != null
    }
    fun logout(){
        auth.signOut()
    }
}

