package com.followapp.mytasks.loginModule.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.followapp.mytasks.R
import com.followapp.mytasks.databinding.FragmentLoginBinding
import com.followapp.mytasks.homeModule.view.HomeFragment
import com.followapp.mytasks.loginModule.model.LoginRepository
import com.followapp.mytasks.loginModule.viewModel.LoginViewModel
import com.followapp.mytasks.loginModule.viewModel.LoginViewModelFactory

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var loginViewModel: LoginViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewModel()
        setupObservers()
        setupListeners()
    }

    private fun setupViewModel() {
        loginViewModel = ViewModelProvider(this, LoginViewModelFactory(LoginRepository()))[LoginViewModel::class.java]
    }

    private fun setupObservers() {
        loginViewModel.playServicesAvailable.observe(viewLifecycleOwner, Observer { available ->
            if (available) {
                loginViewModel.getGoogleIdToken(requireContext())
            } else {
                Toast.makeText(requireContext(), "Google Play Services are required.", Toast.LENGTH_SHORT).show()
            }
        })

        loginViewModel.user.observe(viewLifecycleOwner, Observer { user ->
            if (user != null) {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_view, HomeFragment())
                    .addToBackStack(null)
                    .commit()
            }
        })

        loginViewModel.errorMessage.observe(viewLifecycleOwner, Observer { message ->
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        })
    }

    private fun setupListeners() {
        binding.signInGoogle.setOnClickListener {
            loginViewModel.checkPlayServices(requireContext())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}