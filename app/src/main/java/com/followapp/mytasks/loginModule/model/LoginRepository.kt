package com.followapp.mytasks.loginModule.model

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import com.followapp.mytasks.R
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider

class LoginRepository {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var credentialManager: CredentialManager

    fun checkPlayServices(context: Context): Boolean {
        val googleApiAvailability = GoogleApiAvailability.getInstance()
        val resultCode = googleApiAvailability.isGooglePlayServicesAvailable(context)
        if (resultCode != ConnectionResult.SUCCESS) {
            if (googleApiAvailability.isUserResolvableError(resultCode)) {
                googleApiAvailability.getErrorDialog(context as Activity, resultCode, 9000)?.show()
            } else {
                Toast.makeText(context, "This device is not supported.", Toast.LENGTH_LONG).show()
            }
            return false
        }
        return true
    }

suspend fun getGoogleIdToken(context: Context): GetCredentialResponse? {
    return try {
        credentialManager = CredentialManager.create(context)
        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(context.getString(R.string.web_client_id))
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        credentialManager.getCredential(context, request)
    } catch (e: Exception) {
        Log.e("LoginRepository", "Error getting Google ID token", e)
        null
    }
}

    fun handleSignIn(result: GetCredentialResponse, callback: (FirebaseUser?, String?) -> Unit) {
        when (val credential = result.credential) {
            is GoogleIdTokenCredential -> {
                try {
                    firebaseAuthWithGoogle(credential.idToken, callback)
                } catch (e: GoogleIdTokenParsingException) {
                    callback(null, e.message)
                }
            }

            is CustomCredential -> {
                if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    try {
                        val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                        firebaseAuthWithGoogle(googleIdTokenCredential.idToken, callback)
                    } catch (e: GoogleIdTokenParsingException) {
                        Log.e("LoginFragment", "Received an invalid google id token response", e)
                        callback(null, e.message)
                    }
                }
            }

            else -> {
                Log.e("LoginFragment", "Unexpected type of credential: ${credential::class.java}")
                callback(null, "Unexpected type of credential")
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String, callback: (FirebaseUser?, String?) -> Unit) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                callback(auth.currentUser, null)
            } else {
                callback(null, task.exception?.message)
            }
        }
    }
}