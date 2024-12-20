package com.followapp.mytasks.adModule.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError

class AdViewModel : ViewModel() {

    private val _adLoaded = MutableLiveData<Boolean>()
    val adLoaded: LiveData<Boolean> = _adLoaded

    fun setAdLoaded(value: Boolean) = _adLoaded.postValue(value)

    fun loadAd(adView: AdView) {
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
        adView.adListener = object : AdListener() {
            override fun onAdLoaded() {
                setAdLoaded(true)
            }

            override fun onAdFailedToLoad(error: LoadAdError) {
                setAdLoaded( false)
            }
        }
    }
}