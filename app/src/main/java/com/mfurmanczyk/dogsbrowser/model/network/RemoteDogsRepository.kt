package com.mfurmanczyk.dogsbrowser.model.network

import com.mfurmanczyk.dogsbrowser.model.DogBreed
import com.mfurmanczyk.dogsbrowser.model.DogsRepository
import kotlinx.coroutines.flow.Flow

class RemoteDogsRepository(private val apiHelper: ApiHelper) : DogsRepository {
    override fun getAllDogs(): Flow<List<DogBreed>> = apiHelper.getDogs()

}