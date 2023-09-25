package com.mfurmanczyk.dogsbrowser.model.network

import com.mfurmanczyk.dogsbrowser.model.DogBreed
import kotlinx.coroutines.flow.Flow

interface ApiHelper {
    fun getDogs(): Flow<List<DogBreed>>
}