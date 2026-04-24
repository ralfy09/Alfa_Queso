package com.example.alfaqueso.presentation.pedido

import com.example.alfaqueso.data.remote.dto.PedidoDto
import com.example.alfaqueso.domain.model.DetallePedido

sealed class PedidoUiState {
    object Loading : PedidoUiState()
    data class Success(val pedidos: List<PedidoDto>) : PedidoUiState()
    data class Error(val message: String) : PedidoUiState()
    object Empty : PedidoUiState()
}

data class FormularioPedidoState(
    val clienteNombre: String = "",
    val productos: List<DetallePedido> = emptyList(),
    val total: Double = 0.0
)