package com.mfurmanczyk.dogsbrowser.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mfurmanczyk.dogsbrowser.model.DogBreed
import com.mfurmanczyk.dogsbrowser.model.DogsRepository
import com.mfurmanczyk.dogsbrowser.model.annotation.RemoteDataSource
import com.mfurmanczyk.dogsbrowser.model.annotation.RoomDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface ListUiState {
    data class Success(val dogs: List<DogBreed>) : ListUiState
    data object Loading : ListUiState
    data object Error : ListUiState
}

@HiltViewModel
class ListViewModel @Inject constructor(
    @RemoteDataSource private val remoteRepository: DogsRepository,
    @RoomDataSource private val localRepository: DogsRepository
): ViewModel() {

    val uiState = localRepository.getAllDogs().map {
        if(it.isNotEmpty()) ListUiState.Success(it)
        else ListUiState.Loading
    }.catch {
        ListUiState.Error
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
        ListUiState.Loading
    )

    init {
        refresh()
    }

    fun refresh() {
        fetchFromRemote()
    }

    private fun fetchFromRemote() {
        viewModelScope.launch {
            val dogs = remoteRepository.getAllDogs().first()
            if(dogs.isNotEmpty()) {
                localRepository.deleteAll()
                localRepository.insertAll(*dogs.toTypedArray())
            }
        }
    }

    companion object {
        const val TIMEOUT_MILLIS = 5000L
    }

}