package com.mfurmanczyk.dogsbrowser.model.network

import com.mfurmanczyk.dogsbrowser.model.DogBreed
import retrofit2.http.GET

interface ApiService {
    @GET("DevTides/DogsApi/master/dogs.json")
    suspend fun getDogs() : List<DogBreed>

}