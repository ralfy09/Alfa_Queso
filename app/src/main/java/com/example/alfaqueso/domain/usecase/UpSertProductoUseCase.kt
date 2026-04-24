package com.example.alfaqueso.domain.usecase

import com.example.alfaqueso.domain.repository.ProductoRepository
import com.example.alfaqueso.domain.model.Producto
import javax.inject.Inject

class UpSertProductoUseCase @Inject constructor(
    private val repository: ProductoRepository
) {
    suspend operator fun invoke(producto: Producto) = repository.addProducto(producto)
}