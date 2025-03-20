package com.followapp.mytasks.common.dataAccess.services

import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider

class FirebaseService {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun firebaseAuthWithGoogle(idToken: String, callback: (FirebaseUser?, String?) -> Unit) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                callback(auth.currentUser, null)
            } else {
                callback(null, task.exception?.message)
            }
        }
    }

    fun handleSignIn(result: GoogleIdTokenCredential, callback: (FirebaseUser?, String?) -> Unit) {
        try {
            firebaseAuthWithGoogle(result.idToken, callback)
        } catch (e: GoogleIdTokenParsingException) {
            callback(null, e.message)
        }
    }
}