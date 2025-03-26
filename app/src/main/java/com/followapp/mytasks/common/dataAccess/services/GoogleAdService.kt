package com.followapp.mytasks.common.dataAccess.services

import android.content.Context
import com.followapp.mytasks.common.utils.Constants
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError

class GoogleAdService {

    fun initializeAdView(context: Context): AdView {
        val adView = AdView(context)
        adView.adUnitId = Constants.AD_ID
        adView.setAdSize(AdSize.BANNER)
        return adView
    }

    fun loadAd(adView: AdView, onAdLoaded: () -> Unit, onAdFailed: () -> Unit) {
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
        adView.adListener = object : AdListener() {
            override fun onAdLoaded() {
                onAdLoaded()
            }

            override fun onAdFailedToLoad(error: LoadAdError) {
                onAdFailed()
            }
        }
    }
}