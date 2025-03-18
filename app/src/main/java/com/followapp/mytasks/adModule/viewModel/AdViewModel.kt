package com.followapp.mytasks.adModule.viewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.followapp.mytasks.adModule.model.AdRepository
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError

class AdViewModel(private val repository: AdRepository) : ViewModel() {

    private val _adLoaded = MutableLiveData<Boolean>()
    val adLoaded: LiveData<Boolean> = _adLoaded

    private val _adView = MutableLiveData<AdView>()
    val adView: LiveData<AdView> = _adView

    fun setAdLoaded(value: Boolean) = _adLoaded.postValue(value)

    fun initializeAdView(context: Context, adUnitId: String) {
        val adView = AdView(context)
        adView.setAdSize(AdSize.BANNER)
        adView.adUnitId = adUnitId
        _adView.value = adView
    }

    fun loadAd() {
        _adView.value?.let { adView ->
            val adRequest = AdRequest.Builder().build()
            adView.loadAd(adRequest)
            adView.adListener = object : AdListener() {
                override fun onAdLoaded() {
                    setAdLoaded(true)
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    setAdLoaded(false)
                }
            }
        }
    }
}