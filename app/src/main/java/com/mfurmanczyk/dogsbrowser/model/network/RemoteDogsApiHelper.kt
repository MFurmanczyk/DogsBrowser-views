package com.mfurmanczyk.dogsbrowser.model.network

import com.mfurmanczyk.dogsbrowser.model.DogBreed
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RemoteDogsApiHelper @Inject constructor(
    private val apiService: ApiService
): ApiHelper {
    override fun getDogs(): Flow<List<DogBreed>> = flow {
        emit(apiService.getDogs())
    }
}