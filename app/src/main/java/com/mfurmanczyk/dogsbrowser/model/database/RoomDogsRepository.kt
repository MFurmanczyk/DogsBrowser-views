package com.mfurmanczyk.dogsbrowser.model.database

import com.mfurmanczyk.dogsbrowser.model.DogBreed
import com.mfurmanczyk.dogsbrowser.model.DogsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RoomDogsRepository @Inject constructor(private val dao: DogBreedDao): DogsRepository {
    override fun getAllDogs(): Flow<List<DogBreed>> = dao.getAllDogs()

    override fun getDogById(id: Int): Flow<DogBreed> = dao.getDog(id)

    override suspend fun insertAll(vararg dogs: DogBreed): List<Long> = dao.insertAll(*dogs)

    override suspend fun deleteAll() = dao.deleteAll()
}