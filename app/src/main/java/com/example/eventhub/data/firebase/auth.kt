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
        onResult: (Boolean) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(user.email, password)
            .addOnSuccessListener { result ->
                val uid = result.user?.uid
                uid?.let {
                    firestore.collection("users").document(it).set(user)
                        .addOnSuccessListener {
                            onResult(true)
                            Log.d("Signup", "User saved in Firestore")

                        }
                        .addOnFailureListener {
                            onResult(false)
                            Log.e("Signup", "Firestore error: ${it.message}")

                        }
                }

            }
            .addOnFailureListener {
                onResult(false)
            }
    }
    fun loginfirebase(
        email: String,
        password: String,
        onResult: (Boolean) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                onResult(true)
            }
            .addOnFailureListener {
                onResult(false)
            }
    }

    fun isuserlogged():Boolean{
        return auth.currentUser != null
    }
    fun logout(){
        auth.signOut()
    }
}

