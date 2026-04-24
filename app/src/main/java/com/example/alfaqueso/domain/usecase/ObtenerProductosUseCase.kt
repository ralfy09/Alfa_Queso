package com.example.alfaqueso.domain.usecase

import com.example.alfaqueso.data.mapper.toDomain
import com.example.alfaqueso.domain.model.Producto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ObtenerProductosUseCase @Inject constructor(
    private val repository: ProductoRepositoryImpl
) {
    operator fun invoke(): Flow<List<Producto>> {
        return repository.obtenerProductos().map { lista ->
            lista.map { it.toDomain() }
        }
    }
}