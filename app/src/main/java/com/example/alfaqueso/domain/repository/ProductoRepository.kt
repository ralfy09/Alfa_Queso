package com.example.alfaqueso.domain.repository

import com.example.alfaqueso.domain.model.Producto

interface ProductoRepository {
    suspend fun addProducto(producto: Producto)
    suspend fun getProductos(): List<Producto>
    suspend fun updateProducto(producto: Producto)
    suspend fun deleteProducto(producto: Producto)
}