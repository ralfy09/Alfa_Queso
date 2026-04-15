package com.example.alfaqueso.data.repository

import com.example.alfaqueso.data.local.dao.ProductoDao
import com.example.alfaqueso.data.remote.QuesoApi
import com.example.alfaqueso.data.remote.dto.InventarioDto
import com.example.alfaqueso.domain.model.Inventario
import com.example.alfaqueso.domain.repository.InventarioRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import com.example.alfaqueso.data.mapper.toDomain
import com.example.alfaqueso.data.mapper.toEntity

class InventarioRepositoryImpl @Inject constructor(
    private val api: QuesoApi,
    private val dao: ProductoDao
) : InventarioRepository {
    override fun obtenerInventario(): Flow<List<Inventario>> {
        TODO("Not yet implemented")
    }

    override suspend fun registrarProducto(producto: InventarioDto) {
        TODO("Not yet implemented")
    }

    override suspend fun sincronizarInventario() {
        // Implementación de sincronización con Azure
    }

    override suspend fun actualizarInventarioLocal(productoId: Int, nuevaCantidad: Int) {
        dao.actualizarInventario(productoId, nuevaCantidad)
    }
}
