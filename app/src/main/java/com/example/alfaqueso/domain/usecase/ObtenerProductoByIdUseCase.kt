package com.example.alfaqueso.domain.usecase

import com.example.alfaqueso.domain.repository.ProductoRepository
import javax.inject.Inject

class ObtenerProductoByIdUseCase @Inject constructor(
    private val repository: ProductoRepository
) {
    suspend operator fun invoke(id: Int) = repository.getProductos()
}