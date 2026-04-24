package com.example.alfaqueso.domain.usecase

import com.example.alfaqueso.domain.model.Producto
import com.example.alfaqueso.domain.repository.ProductoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObtenerProductosUseCase @Inject constructor(
    private val repository: ProductoRepository
) {
    operator fun invoke(): Flow<List<Producto>> {
        return repository.obtenerProductos()
    }
}