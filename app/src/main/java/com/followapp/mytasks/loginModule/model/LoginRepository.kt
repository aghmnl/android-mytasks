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
import com.followapp.mytasks.common.dataAccess.services.FirebaseService

import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.firebase.auth.FirebaseUser

class LoginRepository(private val firebaseService: FirebaseService) {

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

    suspend fun signInWithGoogle(context: Context, callback: (FirebaseUser?, String?) -> Unit) {
        val result = getGoogleIdToken(context)
        result?.let {
            handleSignIn(it, callback)
        } ?: run {
            callback(null, "Failed to get Google ID token")
        }
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

    private fun handleSignIn(result: GetCredentialResponse, callback: (FirebaseUser?, String?) -> Unit) {
        when (val credential = result.credential) {
            is GoogleIdTokenCredential -> {
                firebaseService.handleSignIn(credential, callback)
            }

            is CustomCredential -> {
                if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    try {
                        val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                        firebaseService.handleSignIn(googleIdTokenCredential, callback)
                    } catch (e: GoogleIdTokenParsingException) {
                        Log.e("LoginRepository", "Received an invalid google id token response", e)
                        callback(null, e.message)
                    }
                }
            }

            else -> {
                Log.e("LoginRepository", "Unexpected type of credential: ${credential::class.java}")
                callback(null, "Unexpected type of credential")
            }
        }
    }
}