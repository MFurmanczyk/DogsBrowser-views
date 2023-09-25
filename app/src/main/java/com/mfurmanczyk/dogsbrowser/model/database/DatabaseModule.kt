package com.mfurmanczyk.dogsbrowser.model.database

import android.content.Context
import androidx.room.Room
import com.mfurmanczyk.dogsbrowser.model.DogsRepository
import com.mfurmanczyk.dogsbrowser.model.annotation.RoomDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    fun provideDogBreedDao(database: DogsDatabase) = database.dogBreedDao()

    @Provides
    @RoomDataSource
    fun provideDogsRepository(dao: DogBreedDao): DogsRepository = RoomDogsRepository(dao)

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context) : DogsDatabase {
        return Room.databaseBuilder(
            context = context,
            klass = DogsDatabase::class.java,
            "Dogs"
        ).build()
    }
}