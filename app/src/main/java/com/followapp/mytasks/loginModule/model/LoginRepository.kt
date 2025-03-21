package com.followapp.mytasks.loginModule.model

import android.content.Context
import com.followapp.mytasks.common.dataAccess.services.FirebaseService
import com.followapp.mytasks.common.dataAccess.services.GooglePlayService
import com.google.firebase.auth.FirebaseUser

class LoginRepository(
    private val firebaseService: FirebaseService,
    private val googlePlayServicesChecker: GooglePlayService
) {

    suspend fun signInWithGoogle(context: Context, callback: (FirebaseUser?, String?) -> Unit) {
        if (googlePlayServicesChecker.checkPlayServices(context)) {
            val result = firebaseService.getGoogleIdToken(context)
            result?.let {
                firebaseService.handleSignIn(it, callback)
            } ?: run {
                callback(null, "Failed to get Google ID token")
            }
        } else {
            callback(null, "Google Play Services are required.")
        }
    }
}