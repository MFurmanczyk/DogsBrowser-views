package com.mfurmanczyk.dogsbrowser.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mfurmanczyk.dogsbrowser.model.DogBreed
import com.mfurmanczyk.dogsbrowser.model.DogsRepository
import com.mfurmanczyk.dogsbrowser.model.annotation.RemoteDataSource
import com.mfurmanczyk.dogsbrowser.model.annotation.RoomDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.timeout
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.net.UnknownHostException
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

private const val TAG = "ListViewModel"

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

    private val remoteState: MutableStateFlow<ListUiState> = MutableStateFlow(ListUiState.Loading)
    val uiState: StateFlow<ListUiState> = remoteState.combine(localRepository.getAllDogs()) { remoteState, localDogs ->

        when(remoteState) {
            is ListUiState.Success -> remoteState
            is ListUiState.Loading -> remoteState
            is ListUiState.Error -> {
                if(localDogs.isNotEmpty()) {
                    ListUiState.Success(localDogs)
                }
                else ListUiState.Error
            }
        }
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

    @OptIn(FlowPreview::class)
    private fun fetchFromRemote() {
        viewModelScope.launch {
            try {
                remoteState.update {
                    ListUiState.Loading
                }
                val dogs = remoteRepository.getAllDogs()
                    .timeout(15.seconds)
                    .catch {
                        when(it) {
                            is TimeoutCancellationException -> {
                                Log.i(TAG, "fetchFromRemote: ${it.message}")
                                remoteState.update {
                                    ListUiState.Error
                                }
                            }
                            is UnknownHostException -> {
                                Log.i(TAG, "fetchFromRemote: ${it.message}")
                                remoteState.update {
                                    ListUiState.Error
                                }
                            }
                        }
                    }.first()

                cacheLocally(dogs)
                remoteState.update {
                    ListUiState.Success(dogs)
                }
            } catch (e: NoSuchElementException) {
                Log.i(TAG, "fetchFromRemote: ${e.message}")
                remoteState.update {
                    ListUiState.Error
                }
            }
        }
    }

    private suspend fun cacheLocally(dogs: List<DogBreed>) {
        localRepository.deleteAll()
        localRepository.insertAll(*dogs.toTypedArray())
    }

    companion object {
        const val TIMEOUT_MILLIS = 5000L
    }

}