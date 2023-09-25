package com.mfurmanczyk.dogsbrowser.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import com.mfurmanczyk.dogsbrowser.R
import com.mfurmanczyk.dogsbrowser.databinding.FragmentListBinding
import com.mfurmanczyk.dogsbrowser.viewmodel.ListViewModel

class ListFragment : Fragment() {

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    private val viewModel = hiltNavGraphViewModels<ListViewModel>(R.id.dog_navigation)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }
}