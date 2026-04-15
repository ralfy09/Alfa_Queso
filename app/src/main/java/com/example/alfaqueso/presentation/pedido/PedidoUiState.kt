package com.example.alfaqueso.presentation.pedido

import com.example.alfaqueso.domain.model.DetallePedido

data class PedidoUiState(
    val clienteNombre: String = "",
    val productos: List<DetallePedido> = emptyList(),
    val total: Double = 0.0
)