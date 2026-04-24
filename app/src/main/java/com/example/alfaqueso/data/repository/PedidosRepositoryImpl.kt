package com.example.alfaqueso.data.repository

import com.example.alfaqueso.data.local.dao.PedidoDao
import com.example.alfaqueso.data.mapper.toDto
import com.example.alfaqueso.data.mapper.toEntity
import com.example.alfaqueso.data.remote.dto.PedidoDto
import com.example.alfaqueso.domain.repository.PedidosRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PedidosRepositoryImpl @Inject constructor(
    private val dao: PedidoDao
) : PedidosRepository {

    override fun obtenerHistorialPedidos(): Flow<List<PedidoDto>> {
        return dao.obtenerTodosLosPedidos().map { listaEntidades ->
            listaEntidades.map { entidad -> entidad.toDto() }
        }
    }

    override suspend fun registrarPedido(pedido: PedidoDto) {
        dao.insertarPedido(pedido.toEntity())
    }

    override suspend fun actualizarEstadoPedido(id: Int, estado: String) {
        dao.actualizarEstado(id, estado)
    }
}