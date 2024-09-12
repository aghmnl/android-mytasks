package com.followapp.mytasks

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.PasswordCredential
import androidx.credentials.PublicKeyCredential
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import androidx.lifecycle.lifecycleScope
import com.followapp.mytasks.mainModule.view.TasksActivity
import kotlinx.coroutines.launch


class LoginActivity : AppCompatActivity() {

    private lateinit var credentialManager: CredentialManager
    private lateinit var auth: FirebaseAuth      // shared instance of the FirebaseAuth object (the entry point of the Firebase Authentication SDK).


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()
        //auth = Firebase.auth Propuesto por Android Studio
        credentialManager = CredentialManager.create(this)
        initLogin()

    }

    private fun initLogin() {

        // First, check if the user has any accounts that have previously been used to sign in to your app by calling the API with the setFilterByAuthorizedAccounts parameter set to true. Users can choose between available accounts to sign in.
        // If no authorized Google Accounts are available, the user should be prompted to sign up with any of their available accounts. To do this, prompt the user by calling the API again and setting setFilterByAuthorizedAccounts to false
//        val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
//            .setFilterByAuthorizedAccounts(true)
//            .setServerClientId(getString(R.string.web_client_id))
//            .setAutoSelectEnabled(true)
////            .setNonce("nonce string to use when generating a Google ID token")  // To be tested and improved  https://developer.android.com/google/play/integrity/classic#nonce
//            .build()


//        val request: GetCredentialRequest = Builder()
//            .addCredentialOption(googleIdOption)
//            .build()


        /*        Propuesto por Android Studio
            signInRequest = BeginSignInRequest.builder()
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


//        val googleAccount = GoogleSignIn.getLastSignedInAccount(this)
//        if (googleAccount != null) {
//            firebaseAuthWithGoogle(googleAccount.idToken!!)  // If the user is already signed in, authenticate with Firebase.


        // Find the Google Sign-In button in the layout and set a click listener.
        val signInButton: Button = findViewById(R.id.signInGoogle)


        signInButton.setOnClickListener {
            getGoogleIdToken()
        }
    }

    /* Propuesto por Android Studio
    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        updateUI(currentUser);
    }
    */

    private fun getGoogleIdToken() {
        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(getString(R.string.web_client_id))
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        lifecycleScope.launch {
            try {
                val result = credentialManager.getCredential(
                    request = request,
                    context = this@LoginActivity,
                )
                handleSignIn(result)
            } catch (e: GetCredentialException) {
//            handleFailure(e)
            }
        }

//        val task = credentialManager.getCredential(this, request)
//        task.addOnCompleteListener { result ->
//            if (result.isSuccessful) {
//                val idToken = result.result?.idToken
//                if (idToken != null) {
//                    firebaseAuthWithGoogle(idToken)
//                } else {
//                    // Handle case when no Google ID token is available
//                    updateUI(null)
//                }
//            } else {
//                // Handle error
//                updateUI(null)
//            }
//        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser
                updateUI(user)
            } else {
                Log.w(TAG, "signInWithCredential:failure", task.exception)
                updateUI(null)
            }
        }
    }

    private fun handleSignIn(result: GetCredentialResponse) {
        // Handle the successfully returned credential.
        when (val credential = result.credential) {

            // Passkey credential
            is PublicKeyCredential -> {
                // Share responseJson such as a GetCredentialResponse on your server to
                // validate and authenticate
//                responseJson = credential.authenticationResponseJson
            }

            // Password credential
            is PasswordCredential -> {
                // Send ID and password to your server to validate and authenticate.
//                val username = credential.id
//                val password = credential.password
            }

            // GoogleIdToken credential
            is CustomCredential -> {
                if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    try {
                        // Use googleIdTokenCredential and extract id to validate and
                        // authenticate on your server.
                        val googleIdTokenCredential = GoogleIdTokenCredential
                            .createFrom(credential.data)
                        firebaseAuthWithGoogle(googleIdTokenCredential.idToken)

                    } catch (e: GoogleIdTokenParsingException) {
                        Log.e(TAG, "Received an invalid google id token response", e)
                    }
                } else {
                    // Catch any unrecognized custom credential type here.
                    Log.e(TAG, "Unexpected type of credential")
                }
            }

            else -> {
                // Catch any unrecognized credential type here.
                Log.e(TAG, "Unexpected type of credential")
            }
        }
    }


    /* Propuesto por Android Studio
    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.getCurrentUser()
        updateUI(currentUser);
    }

     */

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            val intent = Intent(this, TasksActivity::class.java)

            //  This flag ensures that if an instance of TasksActivity already exists in the back stack, it will be brought to the foreground instead of creating a new one.
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }
    }

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


}


