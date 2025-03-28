package com.followapp.mytasks.adModule.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.followapp.mytasks.adModule.viewModel.AdViewModel
import org.koin.android.ext.android.inject

class AdFragment : Fragment() {

    private val adViewModel: AdViewModel by inject()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                adViewModel.adView.value?.let { adView ->
                    AdScreen(adView = adView)
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel()
        setupObservers()
    }

    private fun setupViewModel() {
        adViewModel.initializeAdView(requireContext())
        adViewModel.loadAd()
    }

    private fun setupObservers() {
        adViewModel.adLoaded.observe(viewLifecycleOwner) { isLoaded ->
            adViewModel.adView.value?.visibility = if (isLoaded) View.VISIBLE else View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        adViewModel.setAdLoaded(false)
        adViewModel.adView.value?.destroy()
    }

    override fun onResume() {
        super.onResume()
        adViewModel.loadAd()
    }
}