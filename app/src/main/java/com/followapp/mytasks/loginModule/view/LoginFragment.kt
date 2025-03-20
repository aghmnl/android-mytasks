package com.followapp.mytasks.loginModule.view

import android.os.Bundle
import android.os.TransactionTooLargeException
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
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
            }
        }
    }

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
                val result = credentialManager.getCredential(requireContext(), request)
                handleSignIn(result)
            } catch (e: GetCredentialException) {
                e.printStackTrace()
            } catch (e: TransactionTooLargeException) {
                Toast.makeText(requireContext(), "Too many accounts or account data is too large. Please delete some accounts or clear Google Play Services data and try again.", Toast.LENGTH_LONG).show()
                e.printStackTrace()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener(requireActivity()) { task ->
            updateUI(if (task.isSuccessful) auth.currentUser else null)
        }
    }

    private fun handleSignIn(result: GetCredentialResponse) {
        when (val credential = result.credential) {
            is GoogleIdTokenCredential -> {
                try {
                    firebaseAuthWithGoogle(credential.idToken)
                } catch (e: GoogleIdTokenParsingException) {
                    e.printStackTrace()
                }
            }
            else -> {
                // Handle other credential types if needed
            }
        }
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container_view, HomeFragment())
                .addToBackStack(null)
                .commit()
        }
    }
}