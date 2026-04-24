package com.example.alfaqueso.domain.usecase

import com.example.alfaqueso.domain.model.Producto
import javax.inject.Inject

class UpSertProductoUseCase @Inject constructor(
    val repository: ProductoRepositoryImpl
) {
    suspend operator fun invoke(producto: Producto) = repository.upSertProducto(producto)
}