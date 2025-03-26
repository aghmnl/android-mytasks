package com.followapp.mytasks.loginModule.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.followapp.mytasks.R
import com.followapp.mytasks.databinding.FragmentLoginBinding
import com.followapp.mytasks.homeModule.view.HomeFragment
import com.followapp.mytasks.loginModule.viewModel.LoginViewModel
import org.koin.android.ext.android.inject

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val loginViewModel: LoginViewModel by inject()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObservers()
        setupListeners()
    }

    private fun setupObservers() {
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
            loginViewModel.signInWithGoogle(requireContext())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}