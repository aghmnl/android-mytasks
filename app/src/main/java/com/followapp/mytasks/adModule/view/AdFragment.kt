package com.followapp.mytasks.adModule.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.followapp.mytasks.adModule.viewModel.AdViewModel
import com.followapp.mytasks.databinding.FragmentAdBinding
import org.koin.android.ext.android.inject

class AdFragment : Fragment() {

    private var _binding: FragmentAdBinding? = null
    private val binding get() = _binding!!

    private val adViewModel: AdViewModel by inject()

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
        adViewModel.initializeAdView(requireContext())
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