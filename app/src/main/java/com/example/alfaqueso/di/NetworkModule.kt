package com.example.alfaqueso.di

import com.example.alfaqueso.data.remote.QuesoApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun proveerQuesoApi(): QuesoApi {
        return Retrofit.Builder()
            .baseUrl("https://proyectoquesos.azurewebsites.net/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(QuesoApi::class.java)
    }
    }