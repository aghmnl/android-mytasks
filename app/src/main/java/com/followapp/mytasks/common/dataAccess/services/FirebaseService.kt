package com.followapp.mytasks.common.dataAccess.services

import android.content.Context
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import com.followapp.mytasks.common.utils.Constants
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider

class FirebaseService {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var credentialManager: CredentialManager

    suspend fun getGoogleIdToken(context: Context): GetCredentialResponse? {
        return try {
            credentialManager = CredentialManager.create(context)
            val googleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(Constants.WEB_CLIENT_ID)
                .build()

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

            credentialManager.getCredential(context, request)
        } catch (e: Exception) {
            Log.e("FirebaseService", "Error getting Google ID token", e)
            null
        }
    }

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

    fun handleSignIn(result: GetCredentialResponse, callback: (FirebaseUser?, String?) -> Unit) {
        when (val credential = result.credential) {
            is GoogleIdTokenCredential -> {
                handleGoogleIdToken(credential, callback)
            }
            is CustomCredential -> {
                handleCustomCredential(credential, callback)
            }
            else -> {
                Log.e("FirebaseService", "Unexpected type of credential: ${credential::class.java}")
                callback(null, "Unexpected type of credential")
            }
        }
    }

    private fun handleGoogleIdToken(credential: GoogleIdTokenCredential, callback: (FirebaseUser?, String?) -> Unit) {
        try {
            firebaseAuthWithGoogle(credential.idToken, callback)
        } catch (e: GoogleIdTokenParsingException) {
            callback(null, e.message)
        }
    }

    private fun handleCustomCredential(credential: CustomCredential, callback: (FirebaseUser?, String?) -> Unit) {
        if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
            try {
                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                firebaseAuthWithGoogle(googleIdTokenCredential.idToken, callback)
            } catch (e: GoogleIdTokenParsingException) {
                Log.e("FirebaseService", "Received an invalid google id token response", e)
                callback(null, e.message)
            }
        }
    }
}