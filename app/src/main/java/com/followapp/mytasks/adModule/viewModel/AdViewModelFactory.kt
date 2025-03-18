package com.followapp.mytasks.adModule.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.followapp.mytasks.adModule.model.AdRepository

class AdViewModelFactory(private val repository: AdRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AdViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AdViewModel(repository) as T
        }
        throw IllegalArgumentException("Clase de ViewModel desconocida")
    }
}