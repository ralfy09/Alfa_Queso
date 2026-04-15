package com.example.alfaqueso.data.repository

import com.example.alfaqueso.data.local.dao.ProductoDao
import com.example.alfaqueso.data.mapper.toEntity
import com.example.alfaqueso.data.remote.QuesoApi
import com.example.alfaqueso.domain.model.Producto
import com.example.alfaqueso.domain.repository.ProductoRepository
import javax.inject.Inject

class ProductoRepositoryImpl @Inject constructor(
    private val api: QuesoApi,
    private val dao: ProductoDao
) {
    suspend fun upSertProducto(producto: Producto) = dao.upSertProducto(producto.toEntity())

    suspend fun obtenerProductoById(id: Int) = dao.obtenerProductoById(id)

    fun obtenerProductos() = dao.obtenerProductos()
}