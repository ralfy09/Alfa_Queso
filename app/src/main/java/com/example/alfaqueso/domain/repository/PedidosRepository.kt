package com.example.alfaqueso.domain.repository

import com.example.alfaqueso.data.remote.dto.PedidoDto
import kotlinx.coroutines.flow.Flow

interface PedidosRepository {
    fun obtenerHistorialPedidos(): Flow<List<PedidoDto>>
    suspend fun registrarPedido(pedido: PedidoDto)
    suspend fun actualizarEstadoPedido(id: Int, estado: String)
}