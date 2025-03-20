package com.followapp.mytasks.loginModule.model

import android.app.Activity
import android.content.Context
import android.widget.Toast
import com.followapp.mytasks.common.dataAccess.services.FirebaseService
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.firebase.auth.FirebaseUser

class LoginRepository(private val firebaseService: FirebaseService) {

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
        val result = firebaseService.getGoogleIdToken(context)
        result?.let {
            firebaseService.handleSignIn(it, callback)
        } ?: run {
            callback(null, "Failed to get Google ID token")
        }
    }
}