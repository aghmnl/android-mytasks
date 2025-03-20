package com.followapp.mytasks.common.dataAccess.services

import android.app.Activity
import android.content.Context
import android.widget.Toast
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability

class GooglePlayService {

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
}