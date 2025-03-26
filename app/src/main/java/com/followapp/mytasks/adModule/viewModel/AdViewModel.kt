package com.followapp.mytasks.adModule.viewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.followapp.mytasks.adModule.model.AdRepository
import com.google.android.gms.ads.AdView

class AdViewModel(private val repository: AdRepository) : ViewModel() {

    private val _adLoaded = MutableLiveData<Boolean>()
    val adLoaded: LiveData<Boolean> = _adLoaded

    private val _adView = MutableLiveData<AdView>()
    val adView: LiveData<AdView> = _adView

    fun setAdLoaded(value: Boolean) = _adLoaded.postValue(value)

    fun initializeAdView(context: Context) {
        _adView.value = repository.initializeAdView(context)
    }

    fun loadAd() {
        _adView.value?.let { adView ->
            repository.loadAd(adView, { setAdLoaded(true) }, { setAdLoaded(false) })
        }
    }
}