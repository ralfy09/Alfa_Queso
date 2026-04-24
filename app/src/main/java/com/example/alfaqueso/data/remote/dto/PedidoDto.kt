package com.example.alfaqueso.data.remote.dto

import kotlinx.serialization.Serializable
@Serializable
data class PedidoDto (
    val pedidoId: Int = 0,
    val clienteId: Int = 0,
    val nombreCliente: String = "",
    val fechaEntrega: String = "",
    val notas: String = "",
    val estado: String = "Pendiente"
)