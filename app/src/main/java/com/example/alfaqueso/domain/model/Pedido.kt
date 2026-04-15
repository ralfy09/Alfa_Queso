package com.example.alfaqueso.domain.model

data class Pedido(
    val id: Int = 0,
    val clienteNombre: String,
    val fecha: Long,
    val total: Double,
    val estado: String,
    val productos: List<DetallePedido>
)