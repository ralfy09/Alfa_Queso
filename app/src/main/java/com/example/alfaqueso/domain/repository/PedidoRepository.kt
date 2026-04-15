package com.example.alfaqueso.domain.repository

import com.example.alfaqueso.domain.model.Pedido

interface PedidoRepository {

    suspend fun guardarPedido(pedido: Pedido)

    suspend fun obtenerPedidos(): List<Pedido>

    suspend fun obtenerPedidoPorId(id: Int): Pedido?

}