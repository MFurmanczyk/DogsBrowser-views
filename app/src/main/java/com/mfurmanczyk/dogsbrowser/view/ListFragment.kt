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
import androidx.recyclerview.widget.LinearLayoutManager
import com.mfurmanczyk.dogsbrowser.R
import com.mfurmanczyk.dogsbrowser.databinding.FragmentListBinding
import com.mfurmanczyk.dogsbrowser.viewmodel.ListUiState
import com.mfurmanczyk.dogsbrowser.viewmodel.ListViewModel
import kotlinx.coroutines.launch

private const val TAG = "ListFragment"

class ListFragment : Fragment() {

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    private val viewModel by hiltNavGraphViewModels<ListViewModel>(R.id.dog_navigation)
    private val dogsListAdapter = DogsListAdapter(arrayListOf())


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.refresh()
        Log.i(TAG, "onViewCreated: refreshing")

        binding.dogsList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = dogsListAdapter
        }

        binding.refreshLayout.setOnRefreshListener {
            viewModel.refresh()
            Log.i(TAG, "onViewCreated: refreshing from listener")
            binding.refreshLayout.isRefreshing = false
        }

        collectState()
    }

    private fun collectState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {

                    Log.i(TAG, "collectState: collecting")

                    binding.listError.visibility = View.GONE
                    binding.loadingView.visibility = View.GONE
                    binding.dogsList.visibility = View.GONE

                    when(it) {
                        is ListUiState.Success -> {
                            binding.dogsList.visibility = View.VISIBLE
                            dogsListAdapter.updateDogList(it.dogs)
                            Log.i(TAG, "collectState: Success")
                        }
                        ListUiState.Error -> {
                            binding.listError.visibility = View.VISIBLE
                            Log.i(TAG, "collectState: Error")
                        }
                        ListUiState.Loading -> {
                            binding.loadingView.visibility = View.VISIBLE
                            Log.i(TAG, "collectState: Loading")
                        }
                    }
                }
            }
        }
    }
}