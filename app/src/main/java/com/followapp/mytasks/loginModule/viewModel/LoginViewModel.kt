package com.followapp.mytasks.loginModule.viewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.followapp.mytasks.loginModule.model.LoginRepository
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    private val repository = LoginRepository()

    private val _playServicesAvailable = MutableLiveData<Boolean>()
    val playServicesAvailable: LiveData<Boolean> get() = _playServicesAvailable

    private val _user = MutableLiveData<FirebaseUser?>()
    val user: LiveData<FirebaseUser?> get() = _user

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    fun checkPlayServices(context: Context) {
        _playServicesAvailable.value = repository.checkPlayServices(context)
    }

    private fun setUser(user: FirebaseUser?) {
        _user.postValue(user)
    }

    private fun setErrorMessage(message: String?) {
        _errorMessage.postValue(message ?: "Unknown error")
    }

    fun getGoogleIdToken(context: Context) {
        viewModelScope.launch {
            try {
                val result = repository.getGoogleIdToken(context)
                repository.handleSignIn(result) { user, error ->
                    setUser(user)
                    error?.let { setErrorMessage(it) }
                }
            } catch (e: Exception) {
                setErrorMessage(e.message)
            }
        }
    }
}