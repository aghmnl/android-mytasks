package com.followapp.mytasks.adModule.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdView

@Composable
fun AdScreen(adView: AdView) {
    AndroidView(factory = { adView })
}