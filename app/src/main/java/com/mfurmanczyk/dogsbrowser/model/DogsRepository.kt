package com.mfurmanczyk.dogsbrowser.model

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

interface DogsRepository {

    /**
     * Retrieves all dogs from datasource.
     */
    fun getAllDogs(): Flow<List<DogBreed>>

    /**
     * Retrieves dog with given [id] from datasource.
     */
    fun getDogById(id: Int): Flow<DogBreed> = flowOf()

    /**
     * Inserts all  [dogs] into datasource
     */
    suspend fun insertAll(vararg dogs: DogBreed): List<Long> = listOf()

    /**
     * Removes all entries from datasource
     */
    suspend fun deleteAll() = Unit
}