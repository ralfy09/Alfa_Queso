package com.example.alfaqueso.domain.repository

import com.example.alfaqueso.domain.model.Producto
import kotlinx.coroutines.flow.Flow

interface ProductoRepository {
    fun obtenerProductos(): Flow<List<Producto>>   // Flow reactivo
    suspend fun addProducto(producto: Producto)
    suspend fun getProductos(): List<Producto>
    suspend fun updateProducto(producto: Producto)
    suspend fun deleteProducto(producto: Producto)
}