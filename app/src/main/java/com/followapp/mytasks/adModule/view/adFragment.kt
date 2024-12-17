package com.followapp.mytasks.adModule.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.followapp.mytasks.adModule.viewModel.AdViewModel
import com.followapp.mytasks.databinding.FragmentAdBinding
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

class AdFragment : Fragment() {

    private var _binding: FragmentAdBinding? = null
    private val binding get() = _binding!!

    private lateinit var adView: AdView
    private lateinit var adViewModel: AdViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentAdBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewModel()
        setupObservers()
        loadAd()
    }

    private fun setupViewModel() {
        adViewModel = ViewModelProvider(this)[AdViewModel::class.java]
    }

    private fun setupObservers() {
        adViewModel.adLoaded.observe(viewLifecycleOwner) { isLoaded ->
            adView.visibility = if (isLoaded) View.VISIBLE else View.GONE
        }
    }

    private fun loadAd() {
        adView = AdView(requireContext())
        adView.setAdSize(AdSize.BANNER)
        adView.adUnitId = "ca-app-pub-3940256099942544/9214589741"  // Banner test AdMob ID
//        advertView.adUnitId = "ca-app-pub-5163472824682213~4420222935"  // FollowApp MyTasks AdMob ID
        binding.adView.addView(adView)  // Add the AdView to the container in the layout
        adViewModel.loadAd(adView)
    }
}
