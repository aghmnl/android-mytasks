package com.followapp.mytasks.loginModule.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.followapp.mytasks.loginModule.model.LoginRepository

class LoginViewModelFactory (private val repository: LoginRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(repository) as T
        }
        throw IllegalArgumentException("Clase de ViewModel desconocida")
    }
}