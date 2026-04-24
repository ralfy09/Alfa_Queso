package com.example.alfaqueso.di

import com.example.alfaqueso.data.repository.ProductoRepositoryImpl
import com.example.alfaqueso.domain.repository.ProductoRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindProductoRepository(
        impl: ProductoRepositoryImpl
    ): ProductoRepository
}