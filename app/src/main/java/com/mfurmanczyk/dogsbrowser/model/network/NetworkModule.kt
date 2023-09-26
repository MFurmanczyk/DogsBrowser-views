package com.mfurmanczyk.dogsbrowser.model.network

import com.mfurmanczyk.dogsbrowser.model.DogsRepository
import com.mfurmanczyk.dogsbrowser.model.annotation.RemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL = "https://raw.githubusercontent.com"

    private val client = OkHttpClient.Builder()
        .readTimeout(timeout = 10, unit = TimeUnit.SECONDS)
        .retryOnConnectionFailure(false)
        .build()

    @Provides
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .client(client)
        .build()

    @Provides
    fun provideApiService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)

    @Provides
    fun provideApiHelper(apiService: ApiService): ApiHelper = RemoteDogsApiHelper(apiService)

    @Provides
    @RemoteDataSource
    fun provideDogsRepository(apiHelper: ApiHelper) : DogsRepository = RemoteDogsRepository(apiHelper = apiHelper)

}