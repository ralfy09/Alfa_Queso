package com.example.alfaqueso.domain.usecase

import javax.inject.Inject

class ObtenerProductoByIdUseCase @Inject constructor(
    private val repository: ProductoRepositoryImpl
) {
    suspend operator fun invoke(id: Int) = repository.obtenerProductoById(id)
}