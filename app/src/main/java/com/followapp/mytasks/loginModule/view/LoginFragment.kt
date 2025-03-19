package com.followapp.mytasks.loginModule.view

import android.os.Bundle
import android.os.TransactionTooLargeException
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.PasswordCredential
import androidx.credentials.PublicKeyCredential
import androidx.credentials.exceptions.GetCredentialException
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.followapp.mytasks.R
import com.followapp.mytasks.homeModule.view.HomeFragment
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {

    private lateinit var credentialManager: CredentialManager
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        credentialManager = CredentialManager.create(requireContext())

//        Log.d("IMPORTANTE", "FirebaseAuth initialized: ${auth != null}")
//        Log.d("IMPORTANTE", "CredentialManager initialized: ${credentialManager != null}")

        initLogin(view)
    }

    private fun checkPlayServices(): Boolean {
        val googleApiAvailability = GoogleApiAvailability.getInstance()
        val resultCode = googleApiAvailability.isGooglePlayServicesAvailable(requireContext())
        if (resultCode != ConnectionResult.SUCCESS) {
            if (googleApiAvailability.isUserResolvableError(resultCode)) {
                googleApiAvailability.getErrorDialog(requireActivity(), resultCode, 9000)?.show()
            } else {
                Toast.makeText(requireContext(), "This device is not supported.", Toast.LENGTH_LONG).show()
            }
            return false
        }
        return true
    }

    private fun initLogin(view: View) {
        val signInButton: Button = view.findViewById(R.id.signInGoogle)
        signInButton.setOnClickListener {
            if (checkPlayServices()) {
                getGoogleIdToken()
            } else {
                Toast.makeText(requireContext(), "Google Play Services are required.", Toast.LENGTH_SHORT).show()
                Log.w("IMPORTANTE", "Google Play Services are required.")
            }
        }
    }

    private fun getGoogleIdToken() {
//        Toast.makeText(requireContext(), "getGoogleIdToken called", Toast.LENGTH_SHORT).show()
        Log.d("IMPORTANTE", "getGoogleIdToken called") // Add this line
        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(getString(R.string.web_client_id))
            .build()

        Log.d("IMPORTANTE", "FilterByAuthorizedAccounts: ${googleIdOption.filterByAuthorizedAccounts}")
        Log.d("IMPORTANTE", "ServerClientId: ${getString(R.string.web_client_id)}")

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        lifecycleScope.launch {
            try {
                Toast.makeText(requireContext(), "Attempting to get credential...", Toast.LENGTH_SHORT).show()
                Log.d("IMPORTANTE", "Attempting to get credential...") // Add this line
                val result = credentialManager.getCredential(
                    request = request,
                    context = requireContext(),
                )
                Toast.makeText(requireContext(), "Credential retrieved successfully", Toast.LENGTH_SHORT).show()
                Log.d("IMPORTANTE", "Credential retrieved successfully") // Add this line
                handleSignIn(result)
            } catch (e: GetCredentialException) {
                Log.e("IMPORTANTE", "Credential exception: ${e.message}", e)
                Log.e("IMPORTANTE", "Credential exception class: ${e.javaClass.name}", e)
                Toast.makeText(requireContext(), "Credential exception: ${e.message}", Toast.LENGTH_SHORT).show()
            } catch (e: TransactionTooLargeException) {
                Log.e("IMPORTANTE", "TransactionTooLargeException: ${e.message}", e)
                Toast.makeText(requireContext(), "Too many accounts or account data is too large. Please try again or clear Google Play Services data.", Toast.LENGTH_LONG).show()
            } catch (e: Exception) {
                Log.e("IMPORTANTE", "General exception: ${e.message}", e)
                Log.e("IMPORTANTE", "General exception class: ${e.javaClass.name}", e)
                Toast.makeText(requireContext(), "General exception: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        Toast.makeText(requireContext(), "firebaseAuthWithGoogle called", Toast.LENGTH_SHORT).show()
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener(requireActivity()) { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser
                Toast.makeText(requireContext(), "Firebase sign-in successful", Toast.LENGTH_SHORT).show()
                updateUI(user)
            } else {
                Log.w("IMPORTANTE", "signInWithCredential:failure", task.exception)
                Toast.makeText(requireContext(), "Firebase sign-in failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                updateUI(null)
            }
        }
    }

    private fun handleSignIn(result: GetCredentialResponse) {
        when (val credential = result.credential) {
            is PublicKeyCredential -> {
                Toast.makeText(requireContext(), "PublicKeyCredential...", Toast.LENGTH_SHORT).show()
            }

            is PasswordCredential -> {
                Toast.makeText(requireContext(), "PasswordCredential...", Toast.LENGTH_SHORT).show()
            }

            is GoogleIdTokenCredential -> {
                Toast.makeText(requireContext(), "GoogleIdTokenCredential...", Toast.LENGTH_SHORT).show()
                try {
                    firebaseAuthWithGoogle(credential.idToken)
                } catch (e: GoogleIdTokenParsingException) {
                    Log.e("IMPORTANTE", "Received an invalid google id token response", e)
                    Toast.makeText(requireContext(), "Invalid google id token: ${e.message}", Toast.LENGTH_SHORT).show()

                }
            }

            is CustomCredential -> {
                Toast.makeText(requireContext(), "CustomCredential...", Toast.LENGTH_SHORT).show()
                if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    try {
                        // Use googleIdTokenCredential and extract id to validate and authenticate on your server.
                        val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                        firebaseAuthWithGoogle(googleIdTokenCredential.idToken)
                    } catch (e: GoogleIdTokenParsingException) {
                        Log.e("IMPORTANTE", "Received an invalid google id token response", e)
                        Toast.makeText(requireContext(), "Invalid google id token: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            else -> {
                // Catch any unrecognized credential type here.
                Log.w("IMPORTANTE", "Unexpected type of credential")
                Toast.makeText(requireContext(), "Unexpected type of credential", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            Toast.makeText(requireContext(), "Navigating to HomeFragment", Toast.LENGTH_SHORT).show()
            val homeFragment = HomeFragment()
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container_view, homeFragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }
}