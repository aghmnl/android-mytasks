package com.followapp.mytasks.loginModule.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.followapp.mytasks.R
import com.followapp.mytasks.homeModule.view.HomeFragment
import com.followapp.mytasks.loginModule.viewModel.LoginViewModel
import org.koin.android.ext.android.inject

class LoginFragment : Fragment() {

    private val loginViewModel: LoginViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupObservers()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                LoginScreen(
                    onSignInClick = { loginViewModel.signInWithGoogle(requireContext()) }
                )
            }
        }
    }

    private fun setupObservers() {
        loginViewModel.user.observe(this, Observer { user ->
            if (user != null) {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_view, HomeFragment())
                    .addToBackStack(null)
                    .commit()
            }
        })

        loginViewModel.errorMessage.observe(this, Observer { message ->
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        })
    }
}