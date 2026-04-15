package com.example.alfaqueso.domain.repository

import com.example.alfaqueso.data.remote.dto.InventarioDto
import com.example.alfaqueso.domain.model.Inventario
import kotlinx.coroutines.flow.Flow

interface InventarioRepository {
    fun obtenerInventario(): Flow<List<Inventario>>
    suspend fun registrarProducto(producto: InventarioDto)
    suspend fun sincronizarInventario()
    suspend fun actualizarInventarioLocal(productoId: Int, nuevaCantidad: Int)
}
