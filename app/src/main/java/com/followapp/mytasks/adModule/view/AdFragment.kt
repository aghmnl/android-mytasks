package com.followapp.mytasks.adModule.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.followapp.mytasks.R
import com.followapp.mytasks.adModule.model.AdRepository
import com.followapp.mytasks.adModule.viewModel.AdViewModel
import com.followapp.mytasks.adModule.viewModel.AdViewModelFactory
import com.followapp.mytasks.databinding.FragmentAdBinding

class AdFragment : Fragment() {

    private var _binding: FragmentAdBinding? = null
    private val binding get() = _binding!!

    private lateinit var adViewModel: AdViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentAdBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewModel()
        setupObservers()
    }

    private fun setupViewModel() {
        adViewModel = ViewModelProvider(this, AdViewModelFactory(AdRepository()))[AdViewModel::class.java]
        adViewModel.initializeAdView(requireContext(), getString(R.string.ad_id))
        adViewModel.loadAd()
    }

    private fun setupObservers() {
        adViewModel.adLoaded.observe(viewLifecycleOwner) { isLoaded ->
            adViewModel.adView.value?.visibility = if (isLoaded) View.VISIBLE else View.GONE
        }
        adViewModel.adView.observe(viewLifecycleOwner) { adView ->
            binding.adView.addView(adView)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        adViewModel.setAdLoaded(false)
        adViewModel.adView.value?.destroy()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        adViewModel.loadAd()
    }
}