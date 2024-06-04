package com.followapp.mytasks

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn  // GoogleSignIn is deprecated in version 21.0.0
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider


class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth      // shared instance of the FirebaseAuth object (the entry point of the Firebase Authentication SDK).

    @Suppress("PrivatePropertyName")     // This is a constant that will be used when calling startActivityForResult(). It's used as a request code.
    // Can be any integer unique to the Activity. Is used to identify the result of the Google Sign-In Intent.
    private val RC_SIGN_IN = 2


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initLogin()

        /* Copilot
//        val keyguardManager = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
//        val intent = keyguardManager.createConfirmDeviceCredentialIntent("Login", "Please confirm your screen lock to continue")
//        startActivityForResult(intent, 0)
         */
    }

    /* Propuesto por Android Studio
    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        updateUI(currentUser);
    }
    */

    private fun initLogin() {
/*        Propuesto por Android Studio
        val signInRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    // Your server's client ID, not your Android client ID.
                    .setServerClientId(getString(R.string.web_client_id))
                    // Only show accounts previously used to sign in.
                    .setFilterByAuthorizedAccounts(true)
                    .build()
            )

 */


        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Configure Google Sign-In to request the user's ID, email address, and basic profile.
        // ID and basic profile are included in DEFAULT_SIGN_IN.
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.web_client_id))  // Request an ID token from Google Play services.
            .requestEmail()   // Request the user's email address.
            .build()   // Build the GoogleSignInOptions.

        val googleSignInClient =
            GoogleSignIn.getClient(this, googleSignInOptions)     // Create a GoogleSignInClient with the options specified by gso.

        // Check for existing Google Sign In account. If the user is already signed in, the GoogleSignInAccount will be non-null.
        val googleAccount = GoogleSignIn.getLastSignedInAccount(this)
        if (googleAccount != null) {
            firebaseAuthWithGoogle(googleAccount.idToken!!)  // If the user is already signed in, authenticate with Firebase.
        }

        // Find the Google Sign-In button in the layout and set a click listener.
        val signInButton: Button = findViewById(R.id.signInGoogle)

        val signInResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                try {
                    val account = task.getResult(ApiException::class.java)!!
                    firebaseAuthWithGoogle(account.idToken!!)
                } catch (e: ApiException) {
                    Log.w(ContentValues.TAG, "Google sign in failed", e)
                    updateUI(null)
                }
            }
        }

        signInButton.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            signInResultLauncher.launch(signInIntent)
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)   // Get a credential from the Google ID token.
        firebaseAuthWithCredential(credential)
    }

    private fun firebaseAuthWithCredential(credential: AuthCredential) {
        auth.signInWithCredential(credential).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                val user = auth.currentUser
                println("PRINTING EMAIL")
                println(user?.email)
                updateUI(user)
            } else {
                // If sign in fails, display a message to the user.
                Log.w(ContentValues.TAG, "signInWithCredential:failure", task.exception)
                updateUI(null)
            }
        }
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) goHome()
    }

    private fun goHome() {
        val intent = Intent(this, TasksActivity::class.java)
        startActivity(intent)
    }

    // This function is called when an activity launched exits, giving the requestCode it started with, the resultCode it returned, and any additional data from it.
    @Deprecated("This method has been deprecated in favor of using the Activity Result API\n      which brings increased type safety via an {@link ActivityResultContract} and the prebuilt\n      contracts for common intents available in\n      {@link androidx.activity.result.contract.ActivityResultContracts}, provides hooks for\n      testing, and allow receiving results in separate, testable classes independent from your\n      activity. Use\n      {@link #registerForActivityResult(ActivityResultContract, ActivityResultCallback)}\n      with the appropriate {@link ActivityResultContract} and handling the result in the\n      {@link ActivityResultCallback#onActivityResult(Object) callback}.")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Check if the request code is the one used for Google Sign-In.
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(ContentValues.TAG, "Google sign in failed", e)
                updateUI(null)
            }
        }
    }

//    @Deprecated(
//        "This method has been deprecated in favor of using the Activity Result API\n      which brings increased type safety via an {@link ActivityResultContract} and the prebuilt\n      contracts for common intents available in\n      {@link androidx.activity.result.contract.ActivityResultContracts}, provides hooks for\n      testing, and allow receiving results in separate, testable classes independent from your\n      activity. Use\n      {@link #registerForActivityResult(ActivityResultContract, ActivityResultCallback)}\n      with the appropriate {@link ActivityResultContract} and handling the result in the\n      {@link ActivityResultCallback#onActivityResult(Object) callback}.",
//        ReplaceWith("super.onActivityResult(requestCode, resultCode, data)", "androidx.appcompat.app.AppCompatActivity")
//    )
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)

    /* Propuesto por Android Studio
    val googleCredential = oneTapClient.getSignInCredentialFromIntent(data)
    val idToken = googleCredential.googleIdToken
    when {
        idToken != null -> {
            // Got an ID token from Google. Use it to authenticate
            // with Firebase.
            val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
            auth.signInWithCredential(firebaseCredential)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithCredential:success")
                        val user = auth.currentUser
                        updateUI(user)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithCredential:failure", task.exception)
                        updateUI(null)
                    }
                }
        }
        else -> {
            // Shouldn't happen.
            Log.d(TAG, "No ID token!")
        }
    }
    */

//        if (requestCode == 0) {
//            if (resultCode == RESULT_OK) {
//                // The user has successfully logged in
//            } else {
//                // The user has failed to log in
//            }
//        }


//    }

    /* Propuesto por Android Studio
    private fun logout() {
        Firebase.auth.signOut()
    }
     */


}
