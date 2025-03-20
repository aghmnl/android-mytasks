package com.followapp.mytasks.loginModule.viewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.followapp.mytasks.loginModule.model.LoginRepository
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel(private val repository: LoginRepository) : ViewModel() {

    private val _user = MutableLiveData<FirebaseUser?>()
    val user: LiveData<FirebaseUser?> get() = _user

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    private fun setUser(user: FirebaseUser?) {
        _user.postValue(user)
    }

    private fun setErrorMessage(message: String?) {
        _errorMessage.postValue(message ?: "Unknown error")
    }

    fun signInWithGoogle(context: Context) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                if (repository.checkPlayServices(context)) {
                    repository.signInWithGoogle(context) { user, error ->
                        setUser(user)
                        error?.let {
                            setErrorMessage(it)
                        }
                    }
                } else {
                    setErrorMessage("Google Play Services are required.")
                }
            }
        }
    }
}