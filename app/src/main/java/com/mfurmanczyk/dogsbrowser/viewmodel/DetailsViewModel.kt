package com.mfurmanczyk.dogsbrowser.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mfurmanczyk.dogsbrowser.model.DogBreed
import com.mfurmanczyk.dogsbrowser.model.DogsRepository
import com.mfurmanczyk.dogsbrowser.model.annotation.RoomDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DetailsUiState(
    val dog: DogBreed? = null,
    val sendSmsStarted: Boolean = false
)

@HiltViewModel
class DetailsViewModel @Inject constructor(
    @RoomDataSource private val repository: DogsRepository
) : ViewModel()  {

    private val _uiState = MutableStateFlow(DetailsUiState())
    val uiState = _uiState.asStateFlow()


    fun getDog(id: Int) {
        viewModelScope.launch {
            repository.getDogById(id).map { dog ->
                _uiState.update {
                    it.copy(dog = dog)
                }
            }.first()
        }
    }

    fun switchSendSms(started: Boolean) {
        _uiState.update {
            it.copy(sendSmsStarted = started)
        }
    }
}