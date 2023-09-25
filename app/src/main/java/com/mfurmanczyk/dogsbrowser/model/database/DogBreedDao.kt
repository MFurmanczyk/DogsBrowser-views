package com.mfurmanczyk.dogsbrowser.model.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.mfurmanczyk.dogsbrowser.model.DogBreed
import kotlinx.coroutines.flow.Flow

@Dao
interface DogBreedDao {

    @Insert
    fun insertAll(vararg dogs: DogBreed): List<Long>

    @Query("SELECT * FROM DogBreed")
    fun getAllDogs(): Flow<List<DogBreed>>

    @Query("SELECT * FROM DogBreed WHERE uuid = :dogId")
    fun getDog(dogId: Int): Flow<DogBreed>

    @Query("DELETE FROM DogBreed")
    suspend fun  deleteAll()
}