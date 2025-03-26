package com.followapp.mytasks.adModule.model

import android.content.Context
import com.followapp.mytasks.common.dataAccess.services.GoogleAdService
import com.google.android.gms.ads.AdView

class AdRepository(private val adService: GoogleAdService) {

    fun initializeAdView(context: Context, adUnitId: String): AdView {
        return adService.initializeAdView(context, adUnitId)
    }

    fun loadAd(adView: AdView, onAdLoaded: () -> Unit, onAdFailed: () -> Unit) {
        adService.loadAd(adView, onAdLoaded, onAdFailed)
    }
}