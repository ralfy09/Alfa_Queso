package com.example.alfaqueso.data.repository

import com.example.alfaqueso.data.local.dao.ProductoDao
import com.example.alfaqueso.data.mapper.toDomain
import com.example.alfaqueso.data.mapper.toEntity
import com.example.alfaqueso.domain.model.Producto
import com.example.alfaqueso.domain.repository.ProductoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ProductoRepositoryImpl @Inject constructor(
    private val dao: ProductoDao
) : ProductoRepository {

    override fun obtenerProductos(): Flow<List<Producto>> {
        return dao.obtenerProductos().map { lista ->
            lista.map { it.toDomain() }
        }
    }

    override suspend fun getProductos(): List<Producto> {
        return dao.obtenerProductos().first().map { it.toDomain() }
    }

    override suspend fun addProducto(producto: Producto) {
        dao.upSertProducto(producto.toEntity())
    }

    override suspend fun updateProducto(producto: Producto) {
        dao.upSertProducto(producto.toEntity())
    }

    override suspend fun deleteProducto(producto: Producto) {
        dao.actualizarInventario(producto.id, 0)
    }

    suspend fun obtenerProductoById(id: Int): Producto {
        return dao.obtenerProductoById(id).toDomain()
    }
}