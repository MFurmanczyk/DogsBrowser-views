package com.mfurmanczyk.dogsbrowser.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.mfurmanczyk.dogsbrowser.R
import com.mfurmanczyk.dogsbrowser.databinding.FragmentDetailsBinding
import com.mfurmanczyk.dogsbrowser.viewmodel.DetailsViewModel
import kotlinx.coroutines.launch

private const val TAG = "DetailsFragment"

class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel by hiltNavGraphViewModels<DetailsViewModel>(R.id.dog_navigation)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            val dogUuid = DetailsFragmentArgs.fromBundle(it).dogUuid
            Log.i(TAG, "onViewCreated: $dogUuid")
            viewModel.getDog(dogUuid)
        }

        collectState()
    }

    private fun collectState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    binding.dog = it.dog
                }
            }
        }
    }
}