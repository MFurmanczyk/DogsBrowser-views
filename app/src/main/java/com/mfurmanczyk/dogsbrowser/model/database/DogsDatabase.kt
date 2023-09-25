package com.mfurmanczyk.dogsbrowser.model.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mfurmanczyk.dogsbrowser.model.DogBreed

@Database(entities = [DogBreed::class], version = 1, exportSchema = false)
abstract class DogsDatabase: RoomDatabase() {
    abstract fun dogBreedDao(): DogBreedDao
}